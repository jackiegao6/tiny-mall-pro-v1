package com.gzc.domain.weixin.service.login;

import com.google.common.cache.Cache;
import com.gzc.domain.weixin.model.entity.req.WeixinQrCodeReq;
import com.gzc.domain.weixin.model.entity.req.WeixinTemplateMessageReq;
import com.gzc.domain.weixin.model.entity.resp.WeixinQrCodeRes;
import com.gzc.domain.weixin.model.entity.resp.WeixinTemplateMessageRes;
import com.gzc.domain.weixin.model.entity.resp.WeixinTokenRes;
import com.gzc.domain.weixin.model.valobj.ActionNameEnum;
import com.gzc.domain.weixin.service.IWeixinApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginService implements ILoginService {


    @Value("${weixin.config.app-id}")
    private String appid;
    @Value("${weixin.config.app-secret}")
    private String appSecret;
    @Value("${weixin.config.template.template-id}")
    private String template_id;
    @Value("${weixin.config.template.template-notify-url}")
    private String url;

    @Resource
    private IWeixinApiService weixinApiService;

    @Resource
    private Cache<String, String> weixinAccessTokenCache;

    @Resource
    private Cache<String, String> openidTokenCache;

    /**
     * 保存开发者的唯一调用凭证 access token
     */
    private void setAccessTokenCache() throws Exception {
        Call<WeixinTokenRes> call = weixinApiService.getAccessToken("client_credential", appid, appSecret);
        WeixinTokenRes weixinTokenRes = call.execute().body();
        assert weixinTokenRes != null;
        String accessToken = weixinTokenRes.getAccess_token();
        weixinAccessTokenCache.put(appid, accessToken);
    }

    /**
     * @return 获取给用户的ticket凭证 用来换取对应的登录二维码
     * 最终在前端获取
     */
    @Override
    public String createQrCodeTicket() throws Exception {

        // 1. 拿到accessToken
        String accessToken = weixinAccessTokenCache.getIfPresent(appid);
        if (null == accessToken) {
            // access token 缓存失效
            setAccessTokenCache();
            accessToken = weixinAccessTokenCache.getIfPresent(appid);
        }

        // 2. 获取Ticket凭证
        WeixinQrCodeReq weixinQrCodeReq = WeixinQrCodeReq.builder()
                .expire_seconds(600)// ten minutes
                .action_name(ActionNameEnum.QR_SCENE)
                .action_info(WeixinQrCodeReq.ActionInfo.builder()
                        .scene(WeixinQrCodeReq.ActionInfo.Scene.builder()
                                .scene_id(100001)
                                .build())
                        .build())
                .build();
        Call<WeixinQrCodeRes> call = weixinApiService.createQrCode(accessToken, weixinQrCodeReq);
        WeixinQrCodeRes weixinQrCodeRes = call.execute().body();
        assert null != weixinQrCodeRes;
        return weixinQrCodeRes.getTicket();
    }

    @Override
    public void saveLoginState(String ticket, String openid) throws Exception {
        openidTokenCache.put(ticket, openid);

        // 1. 获取 accessToken
        String accessToken = weixinAccessTokenCache.getIfPresent(appid);
        if (null == accessToken){
            setAccessTokenCache();
        }

        // 2. 发送模板消息
        WeixinTemplateMessageReq templateMessageDTO = new WeixinTemplateMessageReq();
        templateMessageDTO.setTouser(openid);
        templateMessageDTO.setTemplate_id(template_id);
        templateMessageDTO.setUrl(url);

        Map<String, Map<String, String>> data = new HashMap<>();
        data.put("keyword1", new HashMap<>(){
            {
                put("value", openid);
            }
        });
        templateMessageDTO.setData(data);

        Call<WeixinTemplateMessageRes> call = weixinApiService.sendTemplateMessage(accessToken, templateMessageDTO);

        WeixinTemplateMessageRes res = call.execute().body();
        if (res != null && 0 != res.getErrcode()){
            log.error("模板消息发送失败 errcode: {} errormsg: {}", res.getErrcode(), res.getErrmsg());
            return;
        }
        log.info("发送模板消息成功");
    }

    @Override
    public String checkLogin(String ticket) {
        return openidTokenCache.getIfPresent(ticket);
    }
}

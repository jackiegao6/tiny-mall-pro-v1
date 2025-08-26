package com.gzc.domain.weixin.service;

import com.gzc.domain.weixin.model.entity.req.WeixinQrCodeReq;
import com.gzc.domain.weixin.model.entity.req.WeixinTemplateMessageReq;
import com.gzc.domain.weixin.model.entity.resp.WeixinQrCodeRes;
import com.gzc.domain.weixin.model.entity.resp.WeixinTemplateMessageRes;
import com.gzc.domain.weixin.model.entity.resp.WeixinTokenRes;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @description 微信API服务 retrofit2
 */
public interface IWeixinApiService {

    /**
     * 获取 Access token
     * 全局唯一后台接口调用凭据（Access Token）
     * 文档：<a href="https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Get_access_token.html">Get_access_token</a>
     *
     * @param grantType 填写client_credential
     * @param appId     开发者的唯一凭证
     * @param appSecret 开发者的唯一凭证密钥，即appsecret
     *
     * @return 1-> Access Token 可用于服务端 API 的调用
     *         2-> expires_in 有效期为 7200 秒
     */
    @GET("/cgi-bin/token")
    Call<WeixinTokenRes> getAccessToken(@Query("grant_type") String grantType,
                                        @Query("appid") String appId,
                                        @Query("secret") String appSecret);

    /**
     * 创建二维码 ticket，用于生成带参数的二维码
     * 文档：<a href="https://developers.weixin.qq.com/doc/offiaccount/Account_Management/Generating_a_Parametric_QR_Code.html">Generating_a_Parametric_QR_Code</a>
     * <a href="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET">前端根据凭证展示二维码</a>
     *
     * @param accessToken            getToken 获取的 token 信息
     * @param weixinQrCodeReq 入参对象
     */
    @POST("/cgi-bin/qrcode/create")
    Call<WeixinQrCodeRes> createQrCode(@Query("access_token") String accessToken,
                                       @Body WeixinQrCodeReq weixinQrCodeReq);

    /**
     * 发送微信公众号模板消息
     * 文档：<a href="https://mp.weixin.qq.com/debug/cgi-bin/readtmpl?t=tmplmsg/faq_tmpl">
     *
     * @param accessToken              getToken 获取的 token 信息
     * @param weixinTemplateMessageReq 入参对象
     */
    @POST("/cgi-bin/message/template/send")
    Call<WeixinTemplateMessageRes> sendTemplateMessage(@Query("access_token") String accessToken,
                                                       @Body WeixinTemplateMessageReq weixinTemplateMessageReq);

}

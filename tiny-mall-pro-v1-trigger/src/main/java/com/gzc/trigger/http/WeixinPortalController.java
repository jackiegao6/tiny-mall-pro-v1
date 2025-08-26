package com.gzc.trigger.http;

import com.gzc.domain.weixin.service.login.ILoginService;
import com.gzc.types.sdk.weixin.MessageTextEntity;
import com.gzc.types.sdk.weixin.SignatureUtil;
import com.gzc.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/weixin/portal")
public class WeixinPortalController {

    @Value("${weixin.config.originalid}")
    private String originalid;
    @Value("${weixin.config.token}")
    private String token;
    @Resource
    private ILoginService loginService;

    /**
     * 给微信官方验签使用
     */
    @GetMapping(value = "/receive", produces = "text/plain;charset=utf-8")
    public String validate(@RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            boolean check = SignatureUtil.check(token, signature, timestamp, nonce);
            log.info("微信公众号验签信息完成 状态: {}", check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            log.error("微信公众号验签信息失败 [{}, {}, {}, {}]", signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    /**
     * 处理用户发来的消息
     */
    @PostMapping(value = "/receive", produces = "application/xml; charset=UTF-8")
    public String post(@RequestBody String requestBody,
                     @RequestParam("signature") String signature,
                     @RequestParam("timestamp") String timestamp,
                     @RequestParam("nonce") String nonce,
                     @RequestParam("openid") String openid,
                     @RequestParam(name = "encrypt_type", required = false) String encType,
                     @RequestParam(name = "msg_signature", required = false) String msgSignature) {
        try {
            // 消息转换
            MessageTextEntity message = XmlUtil.xmlToBean(requestBody, MessageTextEntity.class);
            if ("event".equals(message.getMsgType()) && "SCAN".equals(message.getEvent())) {
                loginService.saveLoginState(message.getTicket(), openid);
                return null;
            }else {
                return buildMessageTextEntity(openid, "你好，" + message.getContent());
            }
        } catch (Exception e) {
            // 内部bug msgId msgID 大小写未统一 在这里忽略掉
            return null;
        }
    }

    /**
     * 固定的返回消息内容
     */
    private String buildMessageTextEntity(String openid, String content) {
        MessageTextEntity res = new MessageTextEntity();
        // 公众号分配的ID
        res.setFromUserName(originalid);
        res.setToUserName(openid);
        res.setCreateTime(String.valueOf(System.currentTimeMillis() / 1000L));
        res.setMsgType("text");
        res.setContent(content);
        return XmlUtil.beanToXml(res);
    }
}

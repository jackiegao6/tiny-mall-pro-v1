package com.gzc.trigger.http;

import com.gzc.types.sdk.weixin.MessageTextEntity;
import com.gzc.types.sdk.weixin.SignatureUtil;
import com.gzc.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/weixin/portal/")
public class WeixinPortalController {

    @Value("${weixin.config.originalid}")
    private String originalid;
    @Value("${weixin.config.token}")
    private String token;


    @GetMapping(value = "receive", produces = "text/plain;charset=utf-8")
    public String validate(@RequestParam(value = "signature", required = false) String signature,
                           @RequestParam(value = "timestamp", required = false) String timestamp,
                           @RequestParam(value = "nonce", required = false) String nonce,
                           @RequestParam(value = "echostr", required = false) String echostr) {
        try {
            boolean check = SignatureUtil.check(token, signature, timestamp, nonce);
            log.info("微信公众号验签信息完成 check：{}", check);
            if (!check) {
                return null;
            }
            return echostr;
        } catch (Exception e) {
            log.error("微信公众号验签信息失败 [{}, {}, {}, {}]", signature, timestamp, nonce, echostr, e);
            return null;
        }
    }

    @PostMapping(value = "receive", produces = "application/xml; charset=UTF-8")
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
            return buildMessageTextEntity(openid, "你好，" + message.getContent());
        } catch (Exception e) {
            log.error("接收微信公众号信息请求{}失败 {}", openid, requestBody, e);
            return "";
        }
    }

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

package com.gzc.trigger.http;

import com.gzc.api.response.Response;
import com.gzc.domain.weixin.service.login.ILoginService;
import com.gzc.types.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/login")
public class LoginController {

    @Resource
    private ILoginService loginService;

    /**
     */
    @GetMapping( "/get-qrcode-ticket")
    public Response<String> weixinQrCodeTicket() {
        try {
            String qrCodeTicket = loginService.createQrCodeTicket();
            log.info("生成微信扫码登录 ticket:{}", qrCodeTicket);
            return Response.<String>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(qrCodeTicket)
                    .build();
        } catch (Exception e) {
            log.error("生成微信扫码登录 ticket 失败", e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @GetMapping("/check-login")
    public Response<String> checkLogin(@RequestParam String ticket){
        try {
            String openid = loginService.checkLogin(ticket);
            if (StringUtils.isNotBlank(openid)) {
                return Response.<String>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(openid)
                        .build();
            } else {
                return Response.<String>builder()
                        .code(ResponseCode.NO_LOGIN.getCode())
                        .info(ResponseCode.NO_LOGIN.getInfo())
                        .build();
            }
        } catch (Exception e) {
            log.error("扫码检测登录结果失败 ticket:{}", ticket, e);
            return Response.<String>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}

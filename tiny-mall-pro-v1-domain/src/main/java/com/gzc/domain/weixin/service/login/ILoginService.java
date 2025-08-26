package com.gzc.domain.weixin.service.login;

/**
 * 登录服务
 */
public interface ILoginService {

    /**
     * 借助微信公众号平台
     * 生成ticket 用来获取一个带参数的二维码
     * 进而在前端渲染
     */
    String createQrCodeTicket() throws Exception;

    /**
     * 将ticket 与 openid 绑定
     * 说明该用户已登录 并且发送模板消息
     */
    void saveLoginState(String ticket, String openid) throws Exception;


    String checkLogin(String ticket);

}

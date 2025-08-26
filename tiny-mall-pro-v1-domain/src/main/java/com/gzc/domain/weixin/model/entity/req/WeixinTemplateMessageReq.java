package com.gzc.domain.weixin.model.entity.req;

import lombok.Data;

import java.util.Map;

/**
 * @description 微信模板消息
 */
@Data
public class WeixinTemplateMessageReq {

    private String touser;
    private String template_id;
    private String url;
    private Map<String, Map<String, String>> data;

}

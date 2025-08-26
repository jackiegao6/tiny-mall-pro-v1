package com.gzc.domain.weixin.model.entity.resp;

import lombok.Data;

@Data
public class WeixinTemplateMessageRes {

    private Long msgid;
    private Integer errcode;
    private String errmsg;
}

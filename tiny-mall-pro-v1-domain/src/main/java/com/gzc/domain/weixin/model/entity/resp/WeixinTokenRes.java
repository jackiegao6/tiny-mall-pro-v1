package com.gzc.domain.weixin.model.entity.resp;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeixinTokenRes {

    private String access_token;
    private Integer expires_in;
}

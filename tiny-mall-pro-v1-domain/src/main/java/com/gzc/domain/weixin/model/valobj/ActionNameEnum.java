package com.gzc.domain.weixin.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ActionNameEnum {


    /**
     * 临时二维码
     */
    QR_SCENE("QR_SCENE"),
    QR_STR_SCENE("QR_STR_SCENE"),

    /**
     * 永久二维码
     */
    QR_LIMIT_SCENE("QR_LIMIT_SCENE"),
    QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE"),
    ;

    private String code;
}

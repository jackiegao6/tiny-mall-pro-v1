package com.gzc.domain.order.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MarketTypeVO {

    NORMAL(0, "无营销"),
    GROUP_BUY_MARKET(1, "拼团营销")
    ;

    private final Integer code;
    private final String desc;

    public static MarketTypeVO valueOf(Integer code){
        switch (code){
            case 0:
                return NORMAL;
            case 1:
                return GROUP_BUY_MARKET;
        }
        throw new RuntimeException("error market code!");

    }

}

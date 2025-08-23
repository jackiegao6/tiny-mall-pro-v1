package com.gzc.infrastructure.gateway.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockMarketPayOrderRequestDTO {

    String userId;
    String goodsId;
    Long activityId;
    String teamId;
    String outTradeNo;
    String notifyUrl;

}

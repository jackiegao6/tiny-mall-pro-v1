package com.gzc.infrastructure.gateway.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LockMarketPayOrderResponseDTO {

    private String orderId;
    private BigDecimal originalPrice;
    private BigDecimal deductionPrice;

    private BigDecimal currentPrice;
    /*组队id*/
    private String teamId;
    /** 交易订单状态枚举 */
    private Integer tradeOrderStatus;
}

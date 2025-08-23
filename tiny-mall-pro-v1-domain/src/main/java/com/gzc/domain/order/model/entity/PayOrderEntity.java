package com.gzc.domain.order.model.entity;


import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayOrderEntity {

    // todo 这个PayOrderEntity 和 这个 OrderEntity 太相似
    private String userId;

    private String orderId;

    private String payUrl;

    private OrderStatusVO orderStatus;

    // 营销类型；0无营销、1拼团营销
    private Integer marketType;
    // 营销金额；优惠金额
    private BigDecimal deductionPrice;
    // 支付金额
    private BigDecimal currentPrice;



}

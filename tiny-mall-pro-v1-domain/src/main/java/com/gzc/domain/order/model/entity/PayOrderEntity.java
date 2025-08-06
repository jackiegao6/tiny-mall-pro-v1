package com.gzc.domain.order.model.entity;


import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayOrderEntity {

    private String userId;

    private String orderId;

    private String payUrl;

    private OrderStatusVO orderStatus;


}

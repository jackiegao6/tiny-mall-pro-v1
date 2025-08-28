package com.gzc.domain.order.adapter.repository;

import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.OrderEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;

import java.util.Date;
import java.util.List;

public interface IPayOrderRepository {

    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);

    void doSaveOrder(CreateOrderAggregate orderAggregate);

    OrderEntity queryOrderByOrderId(String orderId);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    void changeOrder2PaySuccess(String orderId, Date orderTime);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrderClose(String orderId);

    void changeOrderList2DealDone(List<String> outTradeNoList);

}

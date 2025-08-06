package com.gzc.domain.order.adapter.repository;

import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.OrderEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;

public interface IPayOrderRepository {

    void doSaveOrder(CreateOrderAggregate orderAggregate);

    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity);
}

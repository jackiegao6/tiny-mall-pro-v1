package com.gzc.domain.order.adapter.repository;

import com.gzc.domain.order.model.entity.req.ShopCartEntity;

public interface IPayOrderRepository {

    void createPayOrder(ShopCartEntity shopCartEntity);


}

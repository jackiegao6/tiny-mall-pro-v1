package com.gzc.domain.order.service.pay;

import com.gzc.domain.order.model.entity.req.ShopCartEntity;

public interface IPayOrderService {


    void createPayOrder(ShopCartEntity shopCartEntity);
}

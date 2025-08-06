package com.gzc.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;

public interface IPayOrderService {

    PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws AlipayApiException;
}

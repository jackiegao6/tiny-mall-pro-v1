package com.gzc.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;

public interface IPayOrderService {

    /**
     * 调用阿里收银台获得支付订单
     *
     * @param shopCartEntity 购物车实体对象
     * @return 支付订单实体
     * @throws AlipayApiException 调用阿里API出错
     */
    PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws AlipayApiException;

}

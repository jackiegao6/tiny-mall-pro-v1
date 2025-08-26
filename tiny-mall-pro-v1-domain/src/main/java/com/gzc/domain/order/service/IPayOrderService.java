package com.gzc.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;

import java.util.Date;
import java.util.List;

public interface IPayOrderService {

    /**
     * 调用阿里收银台 获得支付二维码
     *
     * @param shopCartEntity 购物车实体对象
     * @return 支付订单实体
     * @throws AlipayApiException 调用阿里API出错
     */
    PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws AlipayApiException;

    void changePayOrderSuccess(String orderId, Date orderTime);

    boolean changeOrderClose(String orderId);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

//    String groupBuyNotify(NotifyRequestDTO notifyRequestDTO);

}

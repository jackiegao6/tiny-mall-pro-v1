package com.gzc.domain.order.service;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.gzc.domain.order.adapter.port.IGroupBuyMarketPort;
import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.MarketPayDiscountEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class PayOrderService extends AbstractPayOrderService{

    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;
    private final AlipayClient alipayClient;
    private final IGroupBuyMarketPort groupBuyMarketPort;

    public PayOrderService(IPayOrderRepository payOrderRepository, IProductPort productPort, AlipayClient alipayClient, IGroupBuyMarketPort groupBuyMarketPort) {
        super(payOrderRepository, productPort);
        this.alipayClient = alipayClient;
        this.groupBuyMarketPort = groupBuyMarketPort;
    }

    @Override
    protected void doSaveOrder(CreateOrderAggregate orderAggregate) {
        payOrderRepository.doSaveOrder(orderAggregate);
    }

    @Override
    protected MarketPayDiscountEntity lockMarketPayOrder(String userId, String productId, String teamId, Long activityId, String orderId) {
        return groupBuyMarketPort.lockOrder(userId, productId, teamId, activityId, orderId);
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("order_id", orderId);
        bizContent.put("total_amount", totalAmount.toString());
        bizContent.put("product_name", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();

        PayOrderEntity payOrderEntity = new PayOrderEntity();
        payOrderEntity.setOrderId(orderId);
        payOrderEntity.setPayUrl(form);
        payOrderEntity.setOrderStatus(OrderStatusVO.PAY_WAIT);

        payOrderRepository.updateOrderPayInfo(payOrderEntity);

        return payOrderEntity;
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount, MarketPayDiscountEntity marketPayDiscountEntity) throws AlipayApiException {
        totalAmount = marketPayDiscountEntity == null ? totalAmount : marketPayDiscountEntity.getCurrentPrice();
        return doPrepayOrder(userId, productId, productName, orderId, totalAmount);
    }
}

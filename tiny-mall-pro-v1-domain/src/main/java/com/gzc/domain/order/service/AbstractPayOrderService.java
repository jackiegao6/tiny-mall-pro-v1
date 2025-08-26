package com.gzc.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.*;
import com.gzc.domain.order.model.valobj.MarketTypeVO;
import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPayOrderService implements IPayOrderService {

    protected final IPayOrderRepository payOrderRepository;
    protected final IProductPort productPort;

    @Override
    public PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws AlipayApiException {

        String userId = shopCartEntity.getUserId();
        String productId = shopCartEntity.getProductId();
        String teamId = shopCartEntity.getTeamId();
        Long activityId = shopCartEntity.getActivityId();
        MarketTypeVO marketTypeVO = shopCartEntity.getMarketTypeVO();

        // 1. 规则校验-查询当前用户是否存在掉单和未支付订单
        OrderEntity unpaidOrderEntity = payOrderRepository.queryUnPayOrder(shopCartEntity);

        if (unpaidOrderEntity != null && OrderStatusVO.PAY_WAIT.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("存在未支付订单 userId:{} productId:{} orderId:{}", userId, productId, unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl()) // todo 超时关单
                    .build();
        } else if (unpaidOrderEntity != null && OrderStatusVO.CREATE.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("存在掉单订单 userId:{} productId:{} orderId:{}", userId, productId, unpaidOrderEntity.getOrderId());

            // 营销锁单
            MarketPayDiscountEntity marketPayDiscountEntity = null;
            if (MarketTypeVO.GROUP_BUY_MARKET.equals(marketTypeVO)) {
                marketPayDiscountEntity = this.lockMarketPayOrder(userId, productId, teamId, activityId, unpaidOrderEntity.getOrderId());
            }
            PayOrderEntity payOrderEntity = doPrepayOrder(userId, productId, unpaidOrderEntity.getProductName(), unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getTotalAmount(), marketPayDiscountEntity);

            return PayOrderEntity.builder()
                    .orderId(payOrderEntity.getOrderId())
                    .payUrl(payOrderEntity.getPayUrl())
                    .build();
        }

        // 2. 创建支付单
        ProductEntity productEntity = productPort.queryProductByProductId(productId);
        String productName = productEntity.getProductName();
        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(shopCartEntity, productName);
        String orderId = orderEntity.getOrderId();

        // 营销锁单
        MarketPayDiscountEntity marketPayDiscountEntity = null;
        if (MarketTypeVO.GROUP_BUY_MARKET.equals(marketTypeVO)) {
            marketPayDiscountEntity = this.lockMarketPayOrder(userId, productId, teamId, activityId, orderId);
        }

        // 写入物品的理论值
        orderEntity.setDeductionPrice(marketPayDiscountEntity == null ? new BigDecimal("0") : marketPayDiscountEntity.getDeductionPrice());
        orderEntity.setCurrentPrice(marketPayDiscountEntity == null ?  productEntity.getOriginalPrice() : marketPayDiscountEntity.getCurrentPrice());
        CreateOrderAggregate orderAggregate = CreateOrderAggregate.builder()
                .productEntity(productEntity)
                .orderEntity(orderEntity)
                .build();
        this.doSaveOrder(orderAggregate);
        log.info("生成本地订单 订单状态在这里更新为create");

        PayOrderEntity payOrderEntity = this.doPrepayOrder(userId, productId, productName, orderId, productEntity.getOriginalPrice(), marketPayDiscountEntity);
        log.info("生成支付单 订单状态在这里更新为pay_wait userId: {} orderId: {} payUrl:\n {}", userId, orderId, payOrderEntity.getPayUrl());

        return payOrderEntity;
    }

    protected abstract MarketPayDiscountEntity lockMarketPayOrder(String userId, String productId, String teamId, Long activityId, String orderId);

    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);

    protected abstract PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException;

    protected abstract PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount, MarketPayDiscountEntity marketPayDiscountEntity) throws AlipayApiException;


}

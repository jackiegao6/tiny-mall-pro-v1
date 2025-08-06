package com.gzc.domain.order.service;

import com.alipay.api.AlipayApiException;
import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.OrderEntity;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractPayOrderService implements IPayOrderService {

    protected final IPayOrderRepository repository;
    protected final IProductPort port;


    @Override
    public PayOrderEntity createPayOrder(ShopCartEntity shopCartEntity) throws AlipayApiException {

        String userId = shopCartEntity.getUserId();
        String productId = shopCartEntity.getProductId();

        // 查询当前用户是否存在掉单和未支付订单
        OrderEntity unpaidOrderEntity = repository.queryUnPayOrder(shopCartEntity);

        if (unpaidOrderEntity != null && OrderStatusVO.PAY_WAIT.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("存在未支付订单 userId:{} productId:{} orderId:{}", userId, productId, unpaidOrderEntity.getOrderId());
            return PayOrderEntity.builder()
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl()) // todo 超时怎么办
                    .build();
        } else if (unpaidOrderEntity != null && OrderStatusVO.CREATE.equals(unpaidOrderEntity.getOrderStatusVO())) {
            log.info("存在调单订单 userId:{} productId:{} orderId:{}", userId, productId, unpaidOrderEntity.getOrderId());
            PayOrderEntity payOrderEntity = doPrepayOrder(userId, productId, unpaidOrderEntity.getProductName(), unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getTotalAmount());
            return PayOrderEntity.builder()
                    .orderId(payOrderEntity.getOrderId())
                    .payUrl(payOrderEntity.getPayUrl())
                    .build();
        }

        // 创建支付单
        ProductEntity productEntity = port.queryProductByProductId(productId);
        OrderEntity orderEntity = CreateOrderAggregate.buildOrderEntity(userId, productId, productEntity.getProductName());
        CreateOrderAggregate orderAggregate = CreateOrderAggregate.builder().productEntity(productEntity).orderEntity(orderEntity).build();

        this.doSaveOrder(orderAggregate);
        // 订单状态在这里更新为create
        log.info("生成本地订单");

        PayOrderEntity payOrderEntity = this.doPrepayOrder(shopCartEntity.getUserId(), productEntity.getProductId(), productEntity.getProductName(), orderEntity.getOrderId(), productEntity.getPrice());
        // 订单状态在这里更新为pay_wait

        log.info("生成支付单 userId: {} orderId: {} payUrl:\n {}", shopCartEntity.getUserId(), orderEntity.getOrderId(), payOrderEntity.getPayUrl());

        return payOrderEntity;
    }


    protected abstract void doSaveOrder(CreateOrderAggregate orderAggregate);

    protected abstract PayOrderEntity doPrepayOrder(String userId, String productId, String productName, String orderId, BigDecimal totalAmount) throws AlipayApiException;


}

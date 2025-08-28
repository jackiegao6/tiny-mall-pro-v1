package com.gzc.infrastructure.adapter.repository;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.order.adapter.event.OrderPaySuccessMessageEvent;
import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.aggregate.CreateOrderAggregate;
import com.gzc.domain.order.model.entity.OrderEntity;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.valobj.MarketTypeVO;
import com.gzc.domain.order.model.valobj.OrderStatusVO;
import com.gzc.infrastructure.dao.IPayOrderDao;
import com.gzc.infrastructure.dao.po.PayOrder;
import com.gzc.infrastructure.event.EventPublisher;
import com.gzc.types.event.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
@Slf4j
@RequiredArgsConstructor
public class PayOrderRepository implements IPayOrderRepository {

    private final IPayOrderDao payOrderDao;
    private final EventPublisher eventPublisher;
    private final OrderPaySuccessMessageEvent orderPaySuccessMessageEvent;

    /**
     * 根据用户id 和 商品id 查询未支付订单
     */
    @Override
    public OrderEntity queryUnPayOrder(ShopCartEntity shopCartEntity) {
        PayOrder payOrderPOReq = new PayOrder();
        payOrderPOReq.setUserId(shopCartEntity.getUserId());
        payOrderPOReq.setProductId(shopCartEntity.getProductId());

        PayOrder payOrderPORes = payOrderDao.queryUnPayOrder(payOrderPOReq);
        if (null == payOrderPORes) return null;

        return OrderEntity.builder()
                .productId(payOrderPORes.getProductId())
                .productName(payOrderPORes.getProductName())
                .orderId(payOrderPORes.getOrderId())
                .orderStatusVO(OrderStatusVO.valueOf(payOrderPORes.getStatus()))
                .orderTime(payOrderPORes.getOrderTime())
                .totalAmount(payOrderPORes.getTotalAmount())
                .payUrl(payOrderPORes.getPayUrl())

                .marketType(payOrderPORes.getMarketType())
                .deductionPrice(payOrderPORes.getDeductionPrice())
                .currentPrice(payOrderPORes.getCurrentPrice())
                .build();
    }

    @Override
    public void doSaveOrder(CreateOrderAggregate orderAggregate) {
        ProductEntity productEntity = orderAggregate.getProductEntity();
        OrderEntity orderEntity = orderAggregate.getOrderEntity();

        PayOrder order = new PayOrder();
        order.setUserId(orderEntity.getUserId());
        order.setProductId(productEntity.getProductId());
        order.setProductName(productEntity.getProductName());
        order.setOrderId(orderEntity.getOrderId());
        order.setOrderTime(orderEntity.getOrderTime());
        order.setTotalAmount(productEntity.getOriginalPrice()); // todo 待修改 总金额
        order.setStatus(orderEntity.getOrderStatusVO().getCode());
        order.setMarketType(orderEntity.getMarketType());
        order.setDeductionPrice(orderEntity.getDeductionPrice());
        order.setCurrentPrice(orderEntity.getCurrentPrice());

        payOrderDao.insert(order);
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        PayOrder payOrderReq = PayOrder.builder()
                .orderId(payOrderEntity.getOrderId())
                .status(payOrderEntity.getOrderStatus().getCode())
                .payUrl(payOrderEntity.getPayUrl())
                .build();
        payOrderDao.updateOrderPayInfo(payOrderReq);
    }

    @Override
    public OrderEntity queryOrderByOrderId(String orderId) {
        PayOrder payOrder = payOrderDao.queryOrderByOrderId(orderId);

        if (null == payOrder) return null;
        return OrderEntity.builder()
                .userId(payOrder.getUserId())
                .productId(payOrder.getProductId())
                .productName(payOrder.getProductName())
                .orderId(payOrder.getOrderId())
                .orderTime(payOrder.getOrderTime())
                .totalAmount(payOrder.getTotalAmount())
                .orderStatusVO(OrderStatusVO.valueOf(payOrder.getStatus()))
                .payUrl(payOrder.getPayUrl())
                .marketType(payOrder.getMarketType())
                .deductionPrice(payOrder.getDeductionPrice())
                .currentPrice(payOrder.getCurrentPrice())
                .build();
    }

    /**
     * 回调方法
     * 接收回调消息，变更数据库状态，之后发送MQ消息
     */
    @Override
    public void changeOrder2PaySuccess(String orderId, Date payTime, MarketTypeVO marketTypeVO) {
        PayOrder payOrderReq = new PayOrder();
        payOrderReq.setOrderId(orderId);
        payOrderReq.setPayTime(payTime);
        payOrderDao.changeOrder2PaySuccess(payOrderReq);

        if (MarketTypeVO.NORMAL.equals(marketTypeVO)){
            // 1. 构建BaseEvent 中的data属性对象
            OrderPaySuccessMessageEvent.OrderPaySuccessMessage paySuccessMessage  = OrderPaySuccessMessageEvent.OrderPaySuccessMessage.builder()
                    .orderId(orderId)
                    .build();
            // 2. 进一步包装BaseEvent 此时不仅有data属性 还有 id和时间戳
            BaseEvent.EventMessage<OrderPaySuccessMessageEvent.OrderPaySuccessMessage> paySuccessMessageEvent  = orderPaySuccessMessageEvent.getEventMessage(paySuccessMessage);
            eventPublisher.publishOrderPaySuccessMessage(orderPaySuccessMessageEvent.getTopic(), JSON.toJSONString(paySuccessMessageEvent));
        }
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return payOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return payOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return payOrderDao.changeOrder2Close(orderId);
    }

    @Override
    public void changeOrderList2DealDone(List<String> orderIds) {
        // 更新拼团结算状态
        payOrderDao.changeOrderList2DealDone(orderIds);
        orderIds.forEach(orderId -> {
            OrderPaySuccessMessageEvent.OrderPaySuccessMessage paySuccessMessage  = OrderPaySuccessMessageEvent.OrderPaySuccessMessage.builder()
                    .orderId(orderId)
                    .build();
            BaseEvent.EventMessage<OrderPaySuccessMessageEvent.OrderPaySuccessMessage> paySuccessMessageEvent  = orderPaySuccessMessageEvent.getEventMessage(paySuccessMessage);
            eventPublisher.publishOrderPaySuccessMessage(orderPaySuccessMessageEvent.getTopic(), JSON.toJSONString(paySuccessMessageEvent));

        });


    }
}

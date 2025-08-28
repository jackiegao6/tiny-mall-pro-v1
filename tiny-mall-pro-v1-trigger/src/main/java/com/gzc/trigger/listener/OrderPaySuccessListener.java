package com.gzc.trigger.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.gzc.domain.goods.service.IGoodsService;
import com.gzc.domain.order.adapter.event.OrderPaySuccessMessageEvent;
import com.gzc.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description 支付结算成功回调消息
 */

@Slf4j
@Component
public class OrderPaySuccessListener {

    @Resource
    private IGoodsService goodsService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "${spring.rabbitmq.config.consumer.topic_order_pay_success.queue}"),
                    exchange = @Exchange(name = "${spring.rabbitmq.config.consumer.topic_order_pay_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_order_pay_success.routing_key}"
            )
    )
    public void handleEvent(String paySuccessMessageJson){
        try {
            log.info("该订单的所属团中的所有用户都已支付, 进行该订单的发货服务: {}", paySuccessMessageJson);
            BaseEvent.EventMessage<OrderPaySuccessMessageEvent.OrderPaySuccessMessage> eventMessage =
                    JSON.parseObject(
                            paySuccessMessageJson,
                            new TypeReference<BaseEvent.EventMessage<OrderPaySuccessMessageEvent.OrderPaySuccessMessage>>() {}
                    );
            OrderPaySuccessMessageEvent.OrderPaySuccessMessage data = eventMessage.getData();

            String orderId = data.getOrderId();

            goodsService.beginSeparateShip(orderId);
        } catch (Exception e) {
            log.error("收到支付成功消息失败 {}", paySuccessMessageJson,e);
            throw e;
        }
    }
}

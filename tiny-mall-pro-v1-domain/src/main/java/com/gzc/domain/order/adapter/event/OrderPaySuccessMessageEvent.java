package com.gzc.domain.order.adapter.event;

import com.gzc.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class OrderPaySuccessMessageEvent extends BaseEvent<OrderPaySuccessMessageEvent.OrderPaySuccessMessage> {

    @Value("${spring.rabbitmq.config.producer.topic_order_pay_success.routing_key}")
    private String orderPaySuccessMessageRoutingKey;

    @Override
    public String getTopic() {
        return orderPaySuccessMessageRoutingKey;
    }

    @Override
    public EventMessage<OrderPaySuccessMessage> getEventMessage(OrderPaySuccessMessage data) {
        return EventMessage.<OrderPaySuccessMessage>builder()
                .eventId(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OrderPaySuccessMessage{
        private String userId;
        private String orderId;
    }

}

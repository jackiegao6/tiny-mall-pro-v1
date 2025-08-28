package com.gzc.infrastructure.event;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class EventPublisher {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.config.producer.topic_order_pay_success.exchange}")
    private String exchangeName;

    public void publishOrderPaySuccessMessage(String routingKey, String message){
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, message, m -> {
                m.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return m;
            });
        } catch (AmqpException e) {
            log.error("发送MQ消息失败 routingKey:{} message:{}", routingKey, message, e);
            throw e;
        }

    }

}

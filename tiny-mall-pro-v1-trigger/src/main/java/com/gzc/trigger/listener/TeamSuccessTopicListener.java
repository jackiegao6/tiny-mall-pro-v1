package com.gzc.trigger.listener;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.TeamFinishNotifyRequestDTO;
import com.gzc.domain.order.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class TeamSuccessTopicListener {

    @Resource
    private IPayOrderService orderService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "${spring.rabbitmq.config.consumer.topic_team_success.queue}"),
                    exchange = @Exchange(value = "${spring.rabbitmq.config.consumer.topic_team_success.exchange}", type = ExchangeTypes.TOPIC),
                    key = "${spring.rabbitmq.config.consumer.topic_team_success.routing_key}"
            )
    )
    public void listener(String msg){
        try {
            TeamFinishNotifyRequestDTO teamFinishNotifyRequestDTO = JSON.parseObject(msg, TeamFinishNotifyRequestDTO.class);
            String teamId = teamFinishNotifyRequestDTO.getTeamId();
            List<String> outTradeNoList = teamFinishNotifyRequestDTO.getOutTradeNoList();
            log.info("mq: 接收到组队状态完结的信息 teamId: {} orderIds: {}", teamId, outTradeNoList);
            // 之后把这些订单都更新为Deal_Done状态
            orderService.changeOrderList2DealDone(teamFinishNotifyRequestDTO.getOutTradeNoList());

        } catch (Exception e) {
            log.error("拼团回调，组队完成，结算失败 {}", msg, e);
            throw new RuntimeException(e);
        }

    }
}

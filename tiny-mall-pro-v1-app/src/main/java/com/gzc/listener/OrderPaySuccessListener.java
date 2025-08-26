package com.gzc.listener;


import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderPaySuccessListener {

    @Subscribe
    public void handleEvent(String paySuccessMessage){
        log.info("接收到用户成功支付消息");

    }
}

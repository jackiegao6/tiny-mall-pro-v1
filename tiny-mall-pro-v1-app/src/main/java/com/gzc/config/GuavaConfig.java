package com.gzc.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import com.gzc.trigger.listener.OrderPaySuccessListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaConfig {

    @Bean(name = "weixinAccessTokenCache")
    public Cache<String, String> weixinAccessTokenCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Bean(name = "openidTokenCache")
    public Cache<String, String> openidTokenCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();
    }

    /**
     * 保证本机 消费消息效率
     */
    @Bean(name = "eventBus4OrderPaySuccessListener")
    public EventBus eventBusOrderPaySuccessListener(OrderPaySuccessListener listener){
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }

}

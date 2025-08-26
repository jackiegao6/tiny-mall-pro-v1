package com.gzc.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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

}

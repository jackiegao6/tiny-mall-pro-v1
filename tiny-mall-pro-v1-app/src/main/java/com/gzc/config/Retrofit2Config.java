package com.gzc.config;

import com.gzc.domain.weixin.service.IWeixinApiService;
import com.gzc.infrastructure.gateway.IGroupBuyMarketApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class Retrofit2Config {

    @Value("${app.config.group-buy-market.api-url}")
    private String groupBuyMarketApiUrl;

    @Value("${app.config.weixin.api-url}")
    private String weixinApiUrl;


    @Bean
    public IWeixinApiService weixinApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(weixinApiUrl)
                .addConverterFactory(JacksonConverterFactory.create()).build();

        return retrofit.create(IWeixinApiService.class);
    }

    @Bean
    public IGroupBuyMarketApiService groupBuyMarketApiService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(groupBuyMarketApiUrl)
                .addConverterFactory(JacksonConverterFactory.create()).build();

        return retrofit.create(IGroupBuyMarketApiService.class);
    }

}

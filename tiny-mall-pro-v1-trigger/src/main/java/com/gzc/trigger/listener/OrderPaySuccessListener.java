//package com.gzc.trigger.listener;
//
//import com.google.common.eventbus.Subscribe;
//import com.gzc.domain.goods.service.IGoodsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
//@Slf4j
//@Component
//public class OrderPaySuccessListener {
//
//    @Resource
//    private IGoodsService goodsService;
//
//    @Subscribe
//    public void handleEvent(String teamFinishMessageJson){
//        log.info("接收到组队完结消息 {}", teamFinishMessageJson);
//        // goodsService 操作
//    }
//}

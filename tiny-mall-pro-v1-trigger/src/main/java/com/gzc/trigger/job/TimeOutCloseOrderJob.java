package com.gzc.trigger.job;


import com.gzc.domain.order.service.IPayOrderService;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeOutCloseOrderJob {

    private final IPayOrderService payOrderService;


    @Scheduled(cron = " 0 0/10 * * * ?")
    public void exec(){
        try {
            List<String> orderIds = payOrderService.queryTimeoutCloseOrderList();
            if (null == orderIds || orderIds.isEmpty()){
                log.info("定时任务：暂无超时未支付订单");
            }
            for (String orderId : orderIds) {
                boolean status = payOrderService.changeOrderClose(orderId);
                if (status) {
                    log.info("定时任务：超时30分钟订单关闭 orderId: {}", orderId);
                }else{
                    log.error("定时任务：超时30分钟订单关闭失败 orderId: {}", orderId);
                }
            }
        }catch (Exception e){
            log.error("定时任务：超时30分钟订单关闭失败", e);
        }
    }
}

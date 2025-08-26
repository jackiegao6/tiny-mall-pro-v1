package com.gzc.trigger.http;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.NotifyRequestDTO;
import com.gzc.domain.order.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/pay-mall")
public class GroupBuyController {

    @Resource
    private IPayOrderService payOrderService;

    @RequestMapping(value = "/team_finish", method = RequestMethod.POST)
    public void teamFinish(NotifyRequestDTO notifyRequestDTO){
//        List<String> outTradeNoList = notifyRequestDTO.getOutTradeNoList();
//        payOrderService.teamFinish(outTradeNoList);
    }

    @RequestMapping(value = "/settlement")
    public String settlementFinish(@RequestBody NotifyRequestDTO notifyRequestDTO){
        //todo mq通知 或者 总线通知
        log.info("那边把结算完成了 {}", JSON.toJSONString(notifyRequestDTO));
        return "success";
    }
}

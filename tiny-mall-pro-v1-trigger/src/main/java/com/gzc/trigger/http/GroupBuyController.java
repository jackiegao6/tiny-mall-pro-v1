package com.gzc.trigger.http;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.TeamFinishNotifyRequestDTO;
import com.gzc.api.dto.req.TeamFinishRequestDTO;
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
    private IPayOrderService orderService;

    @RequestMapping(value = "/team-finish", method = RequestMethod.POST)
    public String teamFinish(@RequestBody TeamFinishRequestDTO teamFinishRequestDTO){
        //todo mq通知 或者 总线通知
        log.info("该队伍完结 {}", JSON.toJSONString(teamFinishRequestDTO));
        return "success";
    }

    /**
     * 支持HTTP回调
     */
    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    public String settlementFinish(@RequestBody TeamFinishNotifyRequestDTO teamFinishNotifyRequestDTO){
        log.info("结算完成 {}", JSON.toJSONString(teamFinishNotifyRequestDTO));
        orderService.changeOrderList2DealDone(teamFinishNotifyRequestDTO.getOutTradeNoList());
        return "success";
    }
}

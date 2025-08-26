package com.gzc.trigger.http;


import com.alibaba.fastjson2.JSON;
import com.gzc.api.dto.req.NotifyRequestDTO;
import com.gzc.api.dto.req.TeamFinishRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/pay-mall")
public class GroupBuyController {

    @RequestMapping(value = "/team-finish", method = RequestMethod.POST)
    public String teamFinish(@RequestBody TeamFinishRequestDTO teamFinishRequestDTO){
        //todo mq通知 或者 总线通知
        log.info("该队伍完结 {}", JSON.toJSONString(teamFinishRequestDTO));
        return "success";
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.POST)
    public String settlementFinish(@RequestBody NotifyRequestDTO notifyRequestDTO){
        //todo mq通知 或者 总线通知
        log.info("结算完成 {}", JSON.toJSONString(notifyRequestDTO));
        return "success";
    }
}

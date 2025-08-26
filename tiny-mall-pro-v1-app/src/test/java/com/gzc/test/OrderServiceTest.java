package com.gzc.test;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.order.model.entity.PayOrderEntity;
import com.gzc.domain.order.model.entity.ShopCartEntity;
import com.gzc.domain.order.model.valobj.MarketTypeVO;
import com.gzc.domain.order.service.IPayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceTest {
    @Resource
    private IPayOrderService payOrderService;

    @Test
    public void test_OrderStatus_CREATE_PAY_WAIT() throws Exception {
        ShopCartEntity shopCartEntity = new ShopCartEntity();
        shopCartEntity.setUserId("gzc_1106");
        shopCartEntity.setProductId("9890001");
        shopCartEntity.setTeamId(null);
        shopCartEntity.setActivityId(100123L);
        shopCartEntity.setMarketTypeVO(MarketTypeVO.GROUP_BUY_MARKET);
        log.info("\n请求参数:{}", JSON.toJSONString(shopCartEntity));

        PayOrderEntity payOrderRes = payOrderService.createPayOrder(shopCartEntity);
    }

}
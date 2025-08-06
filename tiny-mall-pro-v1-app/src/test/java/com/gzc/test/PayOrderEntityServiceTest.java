package com.gzc.test;

import com.alibaba.fastjson2.JSON;
import com.gzc.domain.order.model.entity.ShopCartEntity;
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
public class PayOrderEntityServiceTest {
    @Resource
    private IPayOrderService payOrderService;

    @Test
    public void test_createOrder() throws Exception {
        ShopCartEntity shopCartEntity = new ShopCartEntity();
        shopCartEntity.setUserId("gzc");
        shopCartEntity.setProductId("1001");
        payOrderService.createPayOrder(shopCartEntity);
        log.info("请求参数:{}", JSON.toJSONString(shopCartEntity));
    }

}
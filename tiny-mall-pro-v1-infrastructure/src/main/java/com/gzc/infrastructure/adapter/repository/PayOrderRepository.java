package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.order.adapter.repository.IPayOrderRepository;
import com.gzc.domain.order.model.entity.req.ShopCartEntity;
import com.gzc.infrastructure.dao.IPayOrderDao;
import com.gzc.infrastructure.dao.po.PayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
@Slf4j
@RequiredArgsConstructor
public class PayOrderRepository implements IPayOrderRepository {

    private final IPayOrderDao payOrderDao;


    @Override
    public void createPayOrder(ShopCartEntity shopCartEntity) {

        String userId = shopCartEntity.getUserId();
        String productId = shopCartEntity.getProductId();

        String orderId = RandomStringUtils.randomNumeric(16);

        PayOrder payOrderReq = PayOrder.builder()
                .userId(userId)
                .productId(productId)
                .orderId(orderId)
                .orderTime(new Date())
                .status("create")
                .build();
        payOrderDao.insert(payOrderReq);

    }
}

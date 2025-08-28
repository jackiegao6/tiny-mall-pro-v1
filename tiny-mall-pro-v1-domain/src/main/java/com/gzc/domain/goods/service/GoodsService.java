package com.gzc.domain.goods.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoodsService extends AbstractGoodsService{

    @Override
    public void changeOrderDealDone(String orderId) {
        goodsRepository.changeOrderDealDone(orderId);
    }

    @Override
    public void beginSeparateShip(String orderId) {

      log.info("对{}这个订单 进行发货处理", orderId);
    }
}

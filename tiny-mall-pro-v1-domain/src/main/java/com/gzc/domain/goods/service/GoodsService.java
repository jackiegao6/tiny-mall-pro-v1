package com.gzc.domain.goods.service;

import org.springframework.stereotype.Service;

@Service
public class GoodsService extends AbstractGoodsService{

    @Override
    public void changeOrderDealDone(String orderId) {
        goodsRepository.changeOrderDealDone(orderId);
    }
}

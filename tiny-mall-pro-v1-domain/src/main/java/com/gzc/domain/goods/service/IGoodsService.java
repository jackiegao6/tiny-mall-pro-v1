package com.gzc.domain.goods.service;

public interface IGoodsService {

    void changeOrderDealDone(String orderId);

    void beginSeparateShip(String orderId);
}

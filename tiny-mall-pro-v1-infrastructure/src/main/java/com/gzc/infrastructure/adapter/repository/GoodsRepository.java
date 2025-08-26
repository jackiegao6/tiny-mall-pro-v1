package com.gzc.infrastructure.adapter.repository;

import com.gzc.domain.goods.adapter.repository.IGoodsRepository;
import com.gzc.infrastructure.dao.IPayOrderDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class GoodsRepository implements IGoodsRepository {

    @Resource
    private IPayOrderDao payOrderDao;

    @Override
    public void changeOrderDealDone(String orderId) {
        payOrderDao.changeOrder2DealDone(orderId);
    }
}

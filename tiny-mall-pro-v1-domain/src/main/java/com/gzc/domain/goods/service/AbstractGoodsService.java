package com.gzc.domain.goods.service;

import com.gzc.domain.goods.adapter.repository.IGoodsRepository;

import javax.annotation.Resource;

public abstract class AbstractGoodsService implements IGoodsService {

    @Resource
    protected IGoodsRepository goodsRepository;
}

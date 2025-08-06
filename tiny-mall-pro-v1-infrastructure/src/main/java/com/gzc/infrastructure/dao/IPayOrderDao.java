package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IPayOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryUnPayOrder(PayOrder payOrder);
}

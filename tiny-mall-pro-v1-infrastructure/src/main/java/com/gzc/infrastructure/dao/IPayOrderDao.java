package com.gzc.infrastructure.dao;

import com.gzc.infrastructure.dao.po.PayOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IPayOrderDao {

    void insert(PayOrder payOrder);

    PayOrder queryOrderByOrderId(String orderId);

    PayOrder queryUnPayOrder(PayOrder payOrder);

    void updateOrderPayInfo(PayOrder payOrder);

    void changeOrder2PaySuccess(PayOrder payOrderReq);

    List<String> queryNoPayNotifyOrder();

    List<String> queryTimeoutCloseOrderList();

    boolean changeOrder2Close(String orderId);

    void changeOrderList2DealDone(@Param("outTradeNoList") List<String> outTradeNoList);

    void changeOrder2DealDone(String orderId);

}

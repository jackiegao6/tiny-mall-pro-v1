package com.gzc.domain.order.adapter.port;

import com.gzc.domain.order.model.entity.LockOrderAfterEntity;

import java.util.Date;

public interface IGroupBuyMarketPort {

    LockOrderAfterEntity lockOrder(String userId, String productId, String teamId, Long activityId, String orderId);

    void settleOrder(String userId, String orderId, Date payTime);
}

package com.gzc.domain.order.adapter.port;

import com.gzc.domain.order.model.entity.MarketPayDiscountEntity;

public interface IGroupBuyMarketPort {

    MarketPayDiscountEntity lockOrder(String userId, String productId, String teamId, Long activityId, String orderId);

}

package com.gzc.domain.order.adapter.port;

import com.gzc.domain.order.model.entity.MarketPayDiscountEntity;
import com.gzc.domain.order.model.entity.ProductEntity;

public interface IProductPort {


    ProductEntity queryProductByProductId(String productId);

    MarketPayDiscountEntity lockOrder(String userId, String productId, String teamId, Long activityId, String orderId);
}

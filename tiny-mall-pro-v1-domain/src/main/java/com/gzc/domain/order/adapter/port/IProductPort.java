package com.gzc.domain.order.adapter.port;

import com.gzc.domain.order.model.entity.ProductEntity;

public interface IProductPort {
    ProductEntity queryProductByProductId(String productId);
}

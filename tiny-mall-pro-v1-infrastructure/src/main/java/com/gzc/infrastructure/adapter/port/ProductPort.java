package com.gzc.infrastructure.adapter.port;

import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.infrastructure.gateway.ProductRPC;
import com.gzc.infrastructure.gateway.dto.resp.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductPort implements IProductPort {

    private final ProductRPC productRPC;

    @Override
    public ProductEntity queryProductByProductId(String productId) {
        ProductDTO productDTO = productRPC.queryProductByProductId(productId);
        return ProductEntity.builder()
                .productId(productDTO.getProductId())
                .productName(productDTO.getProductName())
                .productDesc(productDTO.getProductDesc())
                .price(productDTO.getPrice())
                .build();
    }

}

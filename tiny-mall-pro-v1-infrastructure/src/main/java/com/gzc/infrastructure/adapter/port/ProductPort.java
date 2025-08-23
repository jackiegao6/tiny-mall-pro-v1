package com.gzc.infrastructure.adapter.port;

import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.infrastructure.gateway.ProductRPC;
import com.gzc.infrastructure.gateway.dto.resp.ProductDescResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductPort implements IProductPort {

    private final ProductRPC productRPC;

    @Override
    public ProductEntity queryProductByProductId(String productId) {
        ProductDescResponseDTO productDTO = productRPC.queryProductByProductId(productId);

        return ProductEntity.builder()
                .productId(productId)
                .productName(productDTO.getProductName())
                .productDesc(productDTO.getProductDesc())
                .originalPrice(productDTO.getOriginalPrice())
                .build();
    }
}

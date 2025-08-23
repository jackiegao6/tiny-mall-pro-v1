package com.gzc.infrastructure.adapter.port;

import com.gzc.api.response.Response;
import com.gzc.domain.order.adapter.port.IProductPort;
import com.gzc.domain.order.model.entity.MarketPayDiscountEntity;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.infrastructure.gateway.IGroupBuyMarketApiService;
import com.gzc.infrastructure.gateway.ProductRPC;
import com.gzc.infrastructure.gateway.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.infrastructure.gateway.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.infrastructure.gateway.dto.resp.ProductDTO;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ProductPort implements IProductPort {

    @Value("${app.config.group-buy-market.notify-url}")
    private String notifyUrl;

    private final ProductRPC productRPC;
    private final IGroupBuyMarketApiService groupBuyMarketApiService;

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

    @Override
    public MarketPayDiscountEntity lockOrder(String userId, String productId, String teamId, Long activityId, String orderId) {
        LockMarketPayOrderRequestDTO requestDTO = LockMarketPayOrderRequestDTO.builder()
                .userId(userId)
                .goodsId(productId)
                .activityId(activityId)
                .teamId(teamId)
                .outTradeNo(orderId)
                .notifyUrl(notifyUrl)
                .build();
        Call<Response<LockMarketPayOrderResponseDTO>> call = groupBuyMarketApiService.lockMarketPayOrder(requestDTO);
        try {
            Response<LockMarketPayOrderResponseDTO> response = call.execute().body();
            if (null == response) return null;

            if (!"0000".equals(response.getCode())){
                throw new AppException(response.getCode(), response.getInfo());
            }

            LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = response.getData();

            return MarketPayDiscountEntity.builder()
                        .originalPrice(lockMarketPayOrderResponseDTO.getOriginalPrice())
                        .deductionPrice(lockMarketPayOrderResponseDTO.getDeductionPrice())
                        .currentPrice(lockMarketPayOrderResponseDTO.getCurrentPrice())
                        .build();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

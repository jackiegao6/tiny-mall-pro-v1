package com.gzc.infrastructure.gateway;

import com.gzc.api.response.Response;
import com.gzc.infrastructure.gateway.dto.req.ProductDescRequestDTO;
import com.gzc.infrastructure.gateway.dto.resp.ProductDescResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductRPC {

    private final IGroupBuyMarketApiService groupBuyMarketApiService;

    public ProductDescResponseDTO queryProductByProductId(String productId){

        Call<Response<ProductDescResponseDTO>> call = groupBuyMarketApiService.getProduct(ProductDescRequestDTO.builder().productId(productId).build());
        try {
            Response<ProductDescResponseDTO> response = call.execute().body();
            ProductDescResponseDTO productDTO = response.getData();
            return productDTO;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

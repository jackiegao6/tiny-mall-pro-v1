package com.gzc.infrastructure.gateway;

import com.gzc.api.response.Response;
import com.gzc.infrastructure.gateway.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.infrastructure.gateway.dto.req.ProductDescRequestDTO;
import com.gzc.infrastructure.gateway.dto.req.SettlementRequestDTO;
import com.gzc.infrastructure.gateway.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.infrastructure.gateway.dto.resp.ProductDescResponseDTO;
import com.gzc.infrastructure.gateway.dto.resp.SettlementResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketApiService {

    @POST("/api/v1/gbm/trade/lock-order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);

    @POST("/api/v1/gbm/trade/get-product")
    Call<Response<ProductDescResponseDTO>> getProduct(@Body ProductDescRequestDTO productDescRequestDTO);

    @POST("/api/v1/gbm/trade/settle-order")
    Call<Response<SettlementResponseDTO>> settleMarketPayOrder(@Body SettlementRequestDTO requestDTO);
}

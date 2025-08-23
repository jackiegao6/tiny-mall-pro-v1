package com.gzc.infrastructure.gateway;

import com.gzc.api.response.Response;
import com.gzc.infrastructure.gateway.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.infrastructure.gateway.dto.resp.LockMarketPayOrderResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IGroupBuyMarketApiService {

    @POST("/api/v1/gbm/trade/lock-order")
    Call<Response<LockMarketPayOrderResponseDTO>> lockMarketPayOrder(@Body LockMarketPayOrderRequestDTO requestDTO);
}

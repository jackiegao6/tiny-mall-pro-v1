package com.gzc.infrastructure.adapter.port;

import com.gzc.api.response.Response;
import com.gzc.domain.order.adapter.port.IGroupBuyMarketPort;
import com.gzc.domain.order.model.entity.LockOrderAfterEntity;
import com.gzc.infrastructure.gateway.IGroupBuyMarketApiService;
import com.gzc.infrastructure.gateway.dto.req.LockMarketPayOrderRequestDTO;
import com.gzc.infrastructure.gateway.dto.req.SettlementRequestDTO;
import com.gzc.infrastructure.gateway.dto.resp.LockMarketPayOrderResponseDTO;
import com.gzc.infrastructure.gateway.dto.resp.SettlementResponseDTO;
import com.gzc.types.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;

import java.io.IOException;
import java.util.Date;


@Slf4j
@RequiredArgsConstructor
@Component
public class GroupBuyMarketPort implements IGroupBuyMarketPort {

    @Value("${app.config.group-buy-market.notify-http-open}")
    private boolean notifyHTTPOpen;

    @Value("${app.config.group-buy-market.notify-url}")
    private String notifyUrl;

    @Value("${spring.rabbitmq.config.producer.topic_order_pay_success.routing_key}")
    private String orderPaySuccessRoutingKey;

    private final IGroupBuyMarketApiService groupBuyMarketApiService;

    @Override
    public LockOrderAfterEntity lockOrder(String userId, String productId, String teamId, Long activityId, String orderId) {
        LockMarketPayOrderRequestDTO requestDTO = new LockMarketPayOrderRequestDTO();
        requestDTO.setUserId(userId);
        requestDTO.setGoodsId(productId);
        requestDTO.setActivityId(activityId);
        requestDTO.setTeamId(teamId);
        requestDTO.setOutTradeNo(orderId);
        if (notifyHTTPOpen){
            requestDTO.setNotifyUrl(notifyUrl);
        }else {
            requestDTO.setNotifyMQ(orderPaySuccessRoutingKey);
        }

        Call<Response<LockMarketPayOrderResponseDTO>> call = groupBuyMarketApiService.lockMarketPayOrder(requestDTO);
        try {
            Response<LockMarketPayOrderResponseDTO> response = call.execute().body();
            if (null == response) return null;

            if (!"0".equals(response.getCode())){
                throw new AppException(response.getCode(), response.getInfo());
            }

            LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO = response.getData();

            return LockOrderAfterEntity.builder()
                    .originalPrice(lockMarketPayOrderResponseDTO.getOriginalPrice())
                    .deductionPrice(lockMarketPayOrderResponseDTO.getDeductionPrice())
                    .currentPrice(lockMarketPayOrderResponseDTO.getCurrentPrice())
                    .build();

        } catch (IOException e) {
            log.error("营销锁单失败{}", userId, e);
        }
        return null;
    }

    @Override
    public void settleOrder(String userId, String orderId, Date payTime) {

        SettlementRequestDTO requestDTO = SettlementRequestDTO.builder()
                .userId(userId)
                .outTradeNo(orderId)
                .orderTime(payTime)
                .build();
        Call<Response<SettlementResponseDTO>> call = groupBuyMarketApiService.settleMarketPayOrder(requestDTO);
        try {
            Response<SettlementResponseDTO> response = call.execute().body();
            if (null == response) return ;
            if (!"0".equals(response.getCode())){
                throw new AppException(response.getCode(), response.getInfo());
            }
        } catch (IOException e) {
            log.error("营销结算失败{}", userId, e);
        }

    }
}

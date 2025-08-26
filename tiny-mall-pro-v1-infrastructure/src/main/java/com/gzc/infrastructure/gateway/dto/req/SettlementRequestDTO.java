package com.gzc.infrastructure.gateway.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementRequestDTO {
    private String userId;
    private String outTradeNo;
    private Date orderTime;
}

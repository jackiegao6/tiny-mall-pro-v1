package com.gzc.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LockOrderAfterEntity {

    private BigDecimal originalPrice;
    private BigDecimal deductionPrice;
    private BigDecimal currentPrice;

}

package com.gzc.infrastructure.gateway.dto.resp;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDescResponseDTO {

    private String productName;
    private String productDesc;
    private BigDecimal originalPrice;
}

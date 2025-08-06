package com.gzc.domain.order.model.aggregate;

import com.gzc.domain.order.model.entity.OrderEntity;
import com.gzc.domain.order.model.entity.ProductEntity;
import com.gzc.domain.order.model.valobj.OrderStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {



    private ProductEntity productEntity;

    private OrderEntity orderEntity;

    public static OrderEntity buildOrderEntity(String userId, String productId, String productName){
        return OrderEntity.builder()
                .userId(userId)
                .productId(productId)
                .productName(productName)
                .orderId(RandomStringUtils.randomNumeric(14))
                .orderTime(new Date())
                .orderStatusVO(OrderStatusVO.CREATE)
                .build();
    }

}

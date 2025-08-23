package com.gzc.domain.order.model.entity;

import com.gzc.domain.order.model.valobj.MarketTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopCartEntity {

    private String userId;
    private String productId;

    // 拼团组队id 可为空 为空时 代表用户首次创建拼团
    private String teamId;
    // 活动id 来自于页面调用拼团试算后 获得的活动id信息
    private Long activityId;
    // 营销类型 正常 拼团
    private MarketTypeVO marketTypeVO;

}






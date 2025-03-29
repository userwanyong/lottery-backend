package com.lottery.domain.activity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author 永
 * 未支付订单实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidQuotaOrderEntity {
    // 用户ID
    private String userId;
    // 订单ID
    private String orderId;
    // 外部透传ID
    private String outBusinessNo;
    // 订单金额
    private BigDecimal payAmount;
}

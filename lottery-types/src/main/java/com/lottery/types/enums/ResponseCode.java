package com.lottery.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 状态码枚举
 *
 * @author 永
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(1000, "调用成功"),
    UN_ERROR(1001, "调用失败"),
    ILLEGAL_PARAMETER(1002, "非法参数"),
    NO_LOGIN(1003, "未登录"),
    STRATEGY_RULE_WEIGHT_IS_NULL(1004, "业务异常，策略规则中 rule_weight 权重规则已适用但未配置"),
    INDEX_DUP(1005, "索引重复"),
    ACTIVITY_STATE_ERROR(1006, "活动未开启（非open状态）"),
    ACTIVITY_DATE_ERROR(1007, "非活动日期范围"),
    ACTIVITY_SKU_STOCK_ERROR(1008, "活动库存不足"),
    ACCOUNT_QUOTA_ERROR(1009,"账户总额度不足"),
    ACCOUNT_MONTH_QUOTA_ERROR(1010,"账户月额度不足"),
    ACCOUNT_DAY_QUOTA_ERROR(1011,"账户日额度不足"),

    ;

    private final int code;

    private final String message;

}

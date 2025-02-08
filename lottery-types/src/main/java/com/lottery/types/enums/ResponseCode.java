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
    ;

    private final int code;

    private final String message;

}

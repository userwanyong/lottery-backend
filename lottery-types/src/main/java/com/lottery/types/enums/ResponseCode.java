package com.lottery.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(1000, "调用成功"),
    UN_ERROR(1001, "调用失败"),
    ILLEGAL_PARAMETER(1002, "非法参数"),
    NO_LOGIN(1003, "未登录"),
    STRATEGY_RULE_WEIGHT_IS_NULL(1004, "业务异常，策略规则中的 rule_weight 已启用但未配置"),
    INDEX_DUP(1005, "索引重复"),
    ACTIVITY_STATE_ERROR(1006, "活动未开启（非 open 状态）"),
    ACTIVITY_DATE_ERROR(1007, "不在活动日期范围内"),
    ACTIVITY_SKU_STOCK_ZERO(1008, "抽奖次数库存不足"),
    ACCOUNT_QUOTA_ERROR(1009, "账户总额度不足"),
    ACCOUNT_MONTH_QUOTA_ERROR(1010, "账户月额度不足"),
    ACCOUNT_DAY_QUOTA_ERROR(1011, "账户日额度不足"),
    ACTIVITY_ORDER_ERROR(1012, "用户抽奖单已使用，不可重复抽奖"),
    USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT(1013, "积分不足"),
    DEGRADE_SWITCH(1014, "活动已降级"),
    RATE_LIMITER(1015, "活动已被限流"),
    HYSTRIX(1016, "服务已被熔断"),
    ACTIVITY_SKU_STOCK_ERROR(2008, "抽奖次数库存异常"),
    DATA_EXIST(2009, "数据已存在"),
    USER_ALREADY_RECEIVE_GIFT(2010, "用户已领取该抽奖额度，无需重复领取"),
    FEATURE_IS_NOT_CONFIGURED(2011, "该功能暂未配置"),
    LOGIN_INFO_EMPTY(2012, "用户名或密码不能为空"),
    PERMISSION_DENIED(2013, "权限不足"),
    LOGIN_FAILED(2014, "用户名或密码错误"),
    REFRESH_TOKEN_INVALID(2015, "refreshToken无效或已过期"),
    AUTH_SERVICE_ERROR(2016, "认证服务异常");

    private final int code;
    private final String message;
}

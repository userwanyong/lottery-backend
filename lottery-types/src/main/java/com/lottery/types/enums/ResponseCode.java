package com.lottery.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(1000, "success"),
    UN_ERROR(1001, "system error"),
    ILLEGAL_PARAMETER(1002, "illegal parameter"),
    NO_LOGIN(1003, "not logged in"),
    STRATEGY_RULE_WEIGHT_IS_NULL(1004, "strategy rule weight is null"),
    INDEX_DUP(1005, "duplicate index"),
    ACTIVITY_STATE_ERROR(1006, "activity is not open"),
    ACTIVITY_DATE_ERROR(1007, "activity date is invalid"),
    ACTIVITY_SKU_STOCK_ZERO(1008, "activity sku stock is empty"),
    ACCOUNT_QUOTA_ERROR(1009, "account total quota is insufficient"),
    ACCOUNT_MONTH_QUOTA_ERROR(1010, "account month quota is insufficient"),
    ACCOUNT_DAY_QUOTA_ERROR(1011, "account day quota is insufficient"),
    ACTIVITY_ORDER_ERROR(1012, "activity order already exists"),
    USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT(1013, "credit is insufficient"),
    DEGRADE_SWITCH(1014, "feature degraded"),
    RATE_LIMITER(1015, "request rate limited"),
    HYSTRIX(1016, "service fallback triggered"),
    ACTIVITY_SKU_STOCK_ERROR(2008, "activity sku stock error"),
    DATA_EXIST(2009, "data already exists"),
    USER_ALREADY_RECEIVE_GIFT(2010, "user already received gift"),
    FEATURE_IS_NOT_CONFIGURED(2011, "feature is not configured"),
    PERMISSION_DENIED(2013, "permission denied"),
    REFRESH_TOKEN_INVALID(2015, "refresh token is invalid or expired"),
    AUTH_SERVICE_ERROR(2016, "auth service error"),
    LOGIN_FAILED(2026, "用户名或密码错误"),
    CODE_SEND_FAILED(2027, "验证码发送失败"),
    CODE_LOGIN_FAILED(2028, "验证码登录失败"),
    OAUTH_LOGIN_FAILED(2029, "第三方登录失败"),
    USER_NOT_FOUND(2030, "用户不存在或已禁用"),
    AUTH_MANAGE_FAILED(2031, "认证管理操作失败");

    private final int code;
    private final String message;
}

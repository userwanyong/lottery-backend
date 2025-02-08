package com.marketing.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 规则过滤枚举
 */
@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {
    ALLOW(9000, "放行"),
    TAKE_OVER(9001, "接管");

    private final int code;
    private final String message;
}

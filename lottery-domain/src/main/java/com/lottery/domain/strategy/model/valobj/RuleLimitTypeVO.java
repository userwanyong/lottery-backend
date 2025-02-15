package com.lottery.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 规则限定枚举值
 */
@Getter
@AllArgsConstructor
public enum RuleLimitTypeVO {
    EQUAL(1, "=="),
    GT(2, ">"),
    LT(3, "<"),
    GTE(4, ">="),
    LTE(5, "<="),
    ENUM(6, "枚举"),
    ;
    private final Integer code;
    private final String message;
}

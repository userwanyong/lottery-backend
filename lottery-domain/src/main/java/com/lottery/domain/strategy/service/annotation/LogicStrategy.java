package com.lottery.domain.strategy.service.annotation;

import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 永
 * 规则模型枚举
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {
    DefaultLogicFilterFactory.LogicModel logicMode();
}

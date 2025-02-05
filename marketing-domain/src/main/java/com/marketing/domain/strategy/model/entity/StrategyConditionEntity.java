package com.marketing.domain.strategy.model.entity;

import lombok.Data;

/**
 * @author 永
 * 策略条件实体
 */
@Data
public class StrategyConditionEntity {
    /** 用户ID */
    private String userId;
    /** 策略ID */
    private Long strategyId;
}

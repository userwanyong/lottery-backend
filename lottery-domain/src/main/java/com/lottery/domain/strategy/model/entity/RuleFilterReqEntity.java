package com.lottery.domain.strategy.model.entity;

import lombok.Data;

/**
 * @author 永
 * 规则过滤参数
 */
@Data
public class RuleFilterReqEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID
     */
    private Integer awardId;
    /**
     * 抽奖规则类型 rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)
     */
    private String ruleModel;
}

package com.marketing.domain.strategy.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 策略奖品实体
 */
@Data
public class StrategyAwardEntity {
    /** 抽奖策略ID */
    private Long strategyId;
    /** 抽奖奖品ID  */
    private Long awardId;
    /** 奖品库存总量 */
    private Integer awardCount;
    /** 奖品库存剩余 */
    private Integer awardCountSurplus;
    /** 奖品中奖概率 */
    private BigDecimal awardRate;
}

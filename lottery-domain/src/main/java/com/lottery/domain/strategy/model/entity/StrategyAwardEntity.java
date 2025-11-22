package com.lottery.domain.strategy.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 策略_奖品实体
 */
@Data
public class StrategyAwardEntity {
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID
     */
    private Long awardId;
    /**
     * 抽奖奖品标题
     */
    private String awardTitle;
    /**
     * 抽奖奖品副标题
     */
    private String awardSubtitle;
    /**
     * 奖品图片
     */
    private String image;
    /**
     * 奖品库存总量
     */
    private Integer awardCount;
    /**
     * 奖品库存剩余
     */
    private Integer awardCountSurplus;
    /**
     * 奖品中奖概率
     */
    private BigDecimal awardRate;
    /**
     * 奖品规则ID
     */
    private Long ruleTreeId;
    /**
     * 奖品配置
     */
    private String awardConfig;
    /**
     * 排序
     */
    private Integer sort;
}

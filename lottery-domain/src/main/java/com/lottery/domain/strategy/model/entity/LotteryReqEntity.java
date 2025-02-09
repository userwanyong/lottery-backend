package com.lottery.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 永
 * 抽奖请求实体
 */
@Data
@Builder
public class LotteryReqEntity {
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
    private Long awardId;
}

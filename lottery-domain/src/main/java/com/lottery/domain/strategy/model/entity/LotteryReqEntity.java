package com.lottery.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 永
 * 抽奖请求实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
     * 活动ID
     */
    private Long activityId;
    /**
     * 抽奖奖品ID
     */
    private Long awardId;
}

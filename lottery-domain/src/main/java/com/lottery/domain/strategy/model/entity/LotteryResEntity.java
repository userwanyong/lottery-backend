package com.lottery.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 永
 * 抽奖响应实体
 */
@Data
@Builder
public class LotteryResEntity {
//    /**
//     * 策略ID
//     */
//    private Long strategyId;

    /**
     * 抽奖奖品ID
     */
    private Long awardId;

//    /**
//     * 奖品对接标识（每一个都是一个对应的发奖策略）
//     */
//    private String awardKey;

//    /**
//     * 奖品配置信息
//     */
//    private String awardConfig;
//
//    /**
//     * 奖品内容描述
//     */
//    private String awardDesc;
}

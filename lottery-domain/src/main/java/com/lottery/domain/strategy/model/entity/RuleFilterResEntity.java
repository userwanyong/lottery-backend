package com.lottery.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 永
 * 规则过滤结果
 */
@Data
@Builder
public class RuleFilterResEntity<T extends RuleFilterResEntity.LotteryEntity> {

    private int code;
    private String message;
    private String ruleModel;
    private T data;

    static public class LotteryEntity {
    }

    // 抽奖之前
    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    static public class LotteryBeforeEntity extends LotteryEntity {
        /**
         * 策略ID
         */
        private Long strategyId;
        /**
         * 权重值Key；用于抽奖时可以选择权重抽奖
         */
        private String ruleWeightValueKey;
        /**
         * 奖品ID；
         */
        private Long awardId;
    }

    // 抽奖之中
    static public class LotteryCenterEntity extends LotteryEntity {
    }

    // 抽奖之后
    static public class LotteryAfterEntity extends LotteryEntity {
    }
}

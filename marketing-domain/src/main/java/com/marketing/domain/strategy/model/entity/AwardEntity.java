package com.marketing.domain.strategy.model.entity;

import lombok.Data;

/**
 * @author 永
 * 策略结果实体
 */
@Data
public class AwardEntity {
    /** 用户ID */
    private String userId;
    /** 奖品ID */
    private Long awardId;
}

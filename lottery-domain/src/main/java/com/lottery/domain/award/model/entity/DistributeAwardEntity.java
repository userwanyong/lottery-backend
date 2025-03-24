package com.lottery.domain.award.model.entity;

import lombok.Data;

/**
 * @author 永
 * 发奖实体
 */
@Data
public class DistributeAwardEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 奖品ID
     */
    private Long awardId;
    /**
     * 奖品配置信息
     */
    private String awardConfig;
}

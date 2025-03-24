package com.lottery.domain.award.model.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 积分发奖实体
 */
@Data
public class UserCreditAwardEntity {
    /** 用户ID */
    private String userId;
    /** 积分值 */
    private BigDecimal creditAmount;
}

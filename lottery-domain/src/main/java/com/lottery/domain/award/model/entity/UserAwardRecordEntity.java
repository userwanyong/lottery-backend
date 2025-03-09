package com.lottery.domain.award.model.entity;

import com.lottery.domain.award.model.valobj.AwardStateVO;
import lombok.Data;

import java.util.Date;

/**
 * @author 永
 */
@Data
public class UserAwardRecordEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖订单ID【作为幂等使用】
     */
    private String orderId;

    /**
     * 奖品ID
     */
    private Integer awardId;

    /**
     * 奖品标题（名称）
     */
    private String awardTitle;

    /**
     * 中奖时间
     */
    private Date awardTime;

    /**
     * 奖品状态；create-创建、completed-发奖完成、fail-发奖失败
     */
    private AwardStateVO awardState;

}

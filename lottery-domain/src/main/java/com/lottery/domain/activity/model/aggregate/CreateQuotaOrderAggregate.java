package com.lottery.domain.activity.model.aggregate;

import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import lombok.Builder;
import lombok.Data;

/**
 * @author 永
 * 活动额度聚合对象
 */
@Data
@Builder
public class CreateQuotaOrderAggregate {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 增加；总次数
     */
    private Integer totalCount;
    /**
     * 增加；日次数
     */
    private Integer dayCount;
    /**
     * 增加；月次数
     */
    private Integer monthCount;
    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;

}

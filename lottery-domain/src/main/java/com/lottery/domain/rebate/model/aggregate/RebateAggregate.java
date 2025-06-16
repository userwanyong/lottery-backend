package com.lottery.domain.rebate.model.aggregate;

import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.entity.TaskEntity;
import lombok.Data;

/**
 * @author 永
 * 返利聚合对象
 */
@Data
public class RebateAggregate {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 返利单实体对象
     */
    private RebateOrderEntity rebateOrderEntity;
    /**
     * 任务实体对象
     */
    private TaskEntity taskEntity;
}

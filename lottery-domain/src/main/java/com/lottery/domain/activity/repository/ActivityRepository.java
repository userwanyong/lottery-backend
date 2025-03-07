package com.lottery.domain.activity.repository;

import com.lottery.domain.activity.model.aggregate.CreateOrderAggregate;
import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author 永
 * 活动仓储接口
 */
public interface ActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);
    ActivityEntity queryActivityByActivityId(Long activityId);
    ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId);
    void doSaveOrder(CreateOrderAggregate createOrderAggregate);
}

package com.lottery.domain.activity.service.quota;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;

/**
 * @author 永
 * 活动额度的支撑类
 */
public class ActivitySupportQuota {
    protected DefaultActivityChainFactory defaultActivityChainFactory;
    protected ActivityRepository activityRepository;
    public ActivitySupportQuota(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        this.activityRepository = activityRepository;
        this.defaultActivityChainFactory = defaultActivityChainFactory;
    }
    public ActivitySkuEntity queryActivitySku(Long sku) {
        return activityRepository.queryActivitySku(sku);
    }
    public ActivityEntity queryActivityByActivityId(Long activityId) {
        return activityRepository.queryActivityByActivityId(activityId);
    }
    public ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId) {
        return activityRepository.queryActivityCountByActivityCountId(activityCountId);
    }
}

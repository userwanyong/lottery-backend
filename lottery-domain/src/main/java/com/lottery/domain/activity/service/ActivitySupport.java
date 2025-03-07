package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.rule.factory.DefaultActivityChainFactory;

/**
 * @author 永
 * 抽奖活动的支撑类
 */
public class ActivitySupport {
    protected DefaultActivityChainFactory defaultActivityChainFactory;
    protected ActivityRepository activityRepository;
    public ActivitySupport(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
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

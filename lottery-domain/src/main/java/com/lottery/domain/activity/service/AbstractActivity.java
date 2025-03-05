package com.lottery.domain.activity.service;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 永
 * 抽奖活动抽象类，定义标准的流程
 */
@Slf4j
public abstract  class AbstractActivity implements Order{

    protected ActivityRepository activityRepository;

    public AbstractActivity(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public ActivityOrderEntity createActivityOrder(ActivityShopCartEntity activityShopCartEntity) {
        // 1. 通过sku查询活动信息
        ActivitySkuEntity activitySkuEntity = activityRepository.queryActivitySku(activityShopCartEntity.getSku());
        // 2. 查询活动信息
        ActivityEntity activityEntity = activityRepository.queryActivityByActivityId(activitySkuEntity.getActivityId());
        // 3. 查询次数信息（用户在活动上可参与的次数）
        ActivityCountEntity activityCountEntity = activityRepository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        log.info("查询结果：{} {} {}", JSON.toJSONString(activitySkuEntity), JSON.toJSONString(activityEntity), JSON.toJSONString(activityCountEntity));

        return ActivityOrderEntity.builder().build();
    }

}

package com.lottery.domain.activity.service.rule;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;

/**
 * @author 永
 * 下单责任链接口
 */
public interface ActivityChain extends ActivityChainArmory {
    boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);
}

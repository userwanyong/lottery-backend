package com.lottery.domain.activity.service.rule.impl;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.service.rule.AbstractActivityChain;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 活动规则过滤节点【日期、状态】
 */
@Slf4j
@Component(Constants.ActivityModel.ACTIVITY_BASE)
public class ActivityBaseChain extends AbstractActivityChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("活动责任链-基础信息【有效期、状态】校验开始。");
        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}

package com.lottery.domain.activity.service.quota.rule.factory;

import com.lottery.domain.activity.service.quota.rule.ActivityChain;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 永
 * 活动-额度领域-责任链工厂
 */
@Service
public class DefaultActivityChainFactory {
    private final ActivityChain activityChain;

    public DefaultActivityChainFactory(Map<String, ActivityChain> activityChainGroup) {
        activityChain = activityChainGroup.get(Constants.ActivityModel.ACTIVITY_BASE);
        activityChain.appendNext(activityChainGroup.get(Constants.ActivityModel.ACTIVITY_SKU_STOCK));
    }

    public ActivityChain openActivityChain() {
        return activityChain;
    }
}

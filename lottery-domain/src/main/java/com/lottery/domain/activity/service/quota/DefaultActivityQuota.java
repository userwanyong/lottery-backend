package com.lottery.domain.activity.service.quota;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.model.valobj.OrderStateVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.ActivitySkuStockService;
import com.lottery.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 永
 * 活动-额度领域-默认实现类
 */
@Service
public class DefaultActivityQuota extends AbstractActivityQuota implements ActivitySkuStockService {

    public DefaultActivityQuota(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(QuotaOrderEntity quotaOrderEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
        activityOrderEntity.setUserId(quotaOrderEntity.getUserId());
        activityOrderEntity.setSku(quotaOrderEntity.getSku());
        activityOrderEntity.setActivityId(activityEntity.getActivityId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setState(OrderStateVO.completed);
        activityOrderEntity.setOutBusinessNo(quotaOrderEntity.getOutBusinessNo());

        return CreateQuotaOrderAggregate.builder()
                .userId(quotaOrderEntity.getUserId())
                .activityId(activitySkuEntity.getActivityId())
                .totalCount(activityCountEntity.getTotalCount())
                .dayCount(activityCountEntity.getDayCount())
                .monthCount(activityCountEntity.getMonthCount())
                .activityOrderEntity(activityOrderEntity)
                .build();

    }

    @Override
    protected void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate) {
        activityRepository.doSaveOrder(createQuotaOrderAggregate);
    }

    @Override
    public ActivitySkuStockKeyVO takeQueueValue() {
        return activityRepository.takeQueueValue();
    }

    @Override
    public void updateActivitySkuStock(Long sku) {
        activityRepository.updateActivitySkuStock(sku);
    }

    @Override
    public void clearActivitySkuStock(Long sku) {
        activityRepository.clearActivitySkuStock(sku);
    }

    @Override
    public void clearQueueValue() {
        activityRepository.clearQueueValue();
    }
}

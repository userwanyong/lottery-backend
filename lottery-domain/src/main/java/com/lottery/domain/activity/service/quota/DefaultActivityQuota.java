package com.lottery.domain.activity.service.quota;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.ActivitySkuStockService;
import com.lottery.domain.activity.service.quota.policy.TradePolicy;
import com.lottery.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 活动-额度领域-默认实现类
 */
@Service
public class DefaultActivityQuota extends AbstractActivityQuota implements ActivitySkuStockService {


    public DefaultActivityQuota(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, TradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActivityChainFactory, tradePolicyGroup);
    }

    @Override
    protected CreateQuotaOrderAggregate buildOrderAggregate(QuotaOrderEntity quotaOrderEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        ActivityOrderEntity activityOrderEntity = new ActivityOrderEntity();
        activityOrderEntity.setUserId(quotaOrderEntity.getUserId());
        activityOrderEntity.setSku(quotaOrderEntity.getSku());
        activityOrderEntity.setActivityId(activityEntity.getId());
        activityOrderEntity.setActivityName(activityEntity.getActivityName());
        activityOrderEntity.setStrategyId(activityEntity.getStrategyId());
        activityOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        activityOrderEntity.setOrderTime(new Date());
        activityOrderEntity.setTotalCount(activityCountEntity.getTotalCount());
        activityOrderEntity.setDayCount(activityCountEntity.getDayCount());
        activityOrderEntity.setMonthCount(activityCountEntity.getMonthCount());
        activityOrderEntity.setPayAmount(activitySkuEntity.getProductAmount());
//        activityOrderEntity.setState(OrderStateVO.completed);
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
    public ActivitySkuStockKeyVO takeQueueValue(Long sku) {
        return activityRepository.takeQueueValue(sku);
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
    public void clearQueueValue(Long sku) {
        activityRepository.clearQueueValue(sku);
    }

    @Override
    public List<Long> querySkuList() {
        return activityRepository.querySkuList();
    }

    @Override
    public void updateQuotaOrder(DeliveryOrderEntity deliveryOrderEntity) {
        activityRepository.updateQuotaOrder(deliveryOrderEntity);
    }

    @Override
    public Integer queryTodayUserLotteryCount(String userId, Long activityId) {
        return activityRepository.queryTodayUserLotteryCount(userId, activityId);
    }

    @Override
    public ActivityAccountEntity queryUserActivityAccount(String userId, Long activityId) {
        return activityRepository.queryUserActivityAccount(userId, activityId);
    }

    @Override
    public Integer queryTotalUserLotteryCount(String userId, Long activityId) {
        return activityRepository.queryTotalUserLotteryCount(userId, activityId);
    }
}

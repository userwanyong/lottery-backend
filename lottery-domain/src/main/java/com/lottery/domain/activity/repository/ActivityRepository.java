package com.lottery.domain.activity.repository;

import com.lottery.domain.activity.model.aggregate.CreatePartakeOrderAggregate;
import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.Date;
import java.util.List;

/**
 * @author 永
 * 活动领域仓储接口
 */
public interface ActivityRepository {
    ActivitySkuEntity queryActivitySku(Long sku);
    ActivityEntity queryActivityByActivityId(Long activityId);
    ActivityCountEntity queryActivityCountByActivityCountId(Long activityCountId);
    void doSaveOrder(CreateQuotaOrderAggregate createQuotaOrderAggregate);

    void cacheActivitySkuStockCount(String key, Integer stockCountSurplus);

    boolean reduceActivitySkuStock(Long sku,String key, Date endDateTime);

    void activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO build);

    ActivitySkuStockKeyVO takeQueueValue();

    void updateActivitySkuStock(Long sku);

    void clearQueueValue();

    void clearActivitySkuStock(Long sku);

    PartakeOrderResEntity queryNoUsedPartakeOrder(PartakeOrderReqEntity reqEntity);

    ActivityAccountEntity queryActivityAccountByUserId(String userId, Long activityId);

    ActivityAccountMonthEntity queryActivityAccountMonthByUserId(String userId, Long activityId, String month);

    ActivityAccountDayEntity queryActivityAccountDayByUserId(String userId, Long activityId, String day);

    void saveCreatePartakeOrderAggregate(CreatePartakeOrderAggregate createPartakeOrderAggregate);

    List<ActivitySkuEntity> queryActivitySkuListByActivityId(Long activityId);
}

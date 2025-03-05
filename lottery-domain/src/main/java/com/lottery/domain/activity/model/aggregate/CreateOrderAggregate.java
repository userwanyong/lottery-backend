package com.lottery.domain.activity.model.aggregate;

import com.lottery.domain.activity.model.entity.ActivityAccountEntity;
import com.lottery.domain.activity.model.entity.ActivityOrderEntity;
import lombok.Data;

/**
 * @author 永
 * 下单聚合对象
 */
@Data
public class CreateOrderAggregate {
    /**
     * 活动账户实体
     */
    private ActivityAccountEntity activityAccountEntity;
    /**
     * 活动订单实体
     */
    private ActivityOrderEntity activityOrderEntity;

}

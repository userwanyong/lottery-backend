package com.lottery.domain.activity.service.armory;

import java.util.Date;

/**
 * @author 永
 * 活动-装配领域-调度接口
 */
public interface ActivityService {
    boolean reduceActivitySkuStock(Long sku, Date endDateTime);
}

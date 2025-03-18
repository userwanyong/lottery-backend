package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;

import java.util.List;

/**
 * @author 永
 * 活动-额度领域-库存操作接口
 */
public interface ActivitySkuStockService {
    ActivitySkuStockKeyVO takeQueueValue(Long sku);

    void updateActivitySkuStock(Long sku);

    void clearActivitySkuStock(Long sku);

    void clearQueueValue(Long sku);

    List<Long> querySkuList();
}

package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.SkuProductEntity;

import java.util.List;

/**
 * @author 永
 * sku商品服务接口
 */
public interface ActivitySkuProductService {

    /**
     * 查询当前活动ID下，sku商品列表
     * @param activityId 活动ID
     * @return 返回sku商品列表
     */
    List<SkuProductEntity> querySkuProductEntityListByActivityId(Long activityId);

}

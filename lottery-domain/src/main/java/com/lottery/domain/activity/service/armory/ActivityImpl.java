package com.lottery.domain.activity.service.armory;

import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author 永
 * 活动装配实现类
 */
@Slf4j
@Service
public class ActivityImpl implements ActivityArmory,ActivityService{

    @Resource
    private ActivityRepository repository;
    @Override
    public boolean assembleActivitySku(Long sku) {
        //预热活动sku库存
        ActivitySkuEntity activitySkuEntity = repository.queryActivitySku(sku);
        cacheActivitySkuStockCount(sku,activitySkuEntity.getStockCountSurplus());
        //预热活动(已在查询时放入缓存)
        repository.queryActivityByActivityId(activitySkuEntity.getActivityId());
        //预热次数(已在查询时放入缓存)
        repository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        return true;
    }

    @Override
    public boolean assembleActivitySkuByActivityId(Long activityId) {
        //查询该活动下的sku列表
        List<ActivitySkuEntity> activitySkuEntityList = repository.queryActivitySkuListByActivityId(activityId);
        for (ActivitySkuEntity activitySkuEntity : activitySkuEntityList) {
            cacheActivitySkuStockCount(activitySkuEntity.getSku(),activitySkuEntity.getStockCountSurplus());
            //预热次数(已在查询时放入缓存)
            repository.queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());
        }
        //预热活动(已在查询时放入缓存)
        repository.queryActivityByActivityId(activityId);
        return true;
    }

    private void cacheActivitySkuStockCount(Long sku, Integer stockCountSurplus) {
        String key = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        repository.cacheActivitySkuStockCount(key,stockCountSurplus);
    }


    @Override
    public boolean reduceActivitySkuStock(Long sku, Date endDateTime) {
        String key = Constants.RedisKey.ACTIVITY_SKU_STOCK_COUNT_KEY + sku;
        return repository.reduceActivitySkuStock(sku,key,endDateTime);
    }
}

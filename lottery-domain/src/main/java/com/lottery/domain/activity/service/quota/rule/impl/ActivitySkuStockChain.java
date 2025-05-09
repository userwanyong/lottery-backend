package com.lottery.domain.activity.service.quota.rule.impl;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.armory.ActivityService;
import com.lottery.domain.activity.service.quota.rule.AbstractActivityChain;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 商品库存扣减节点
 */
@Slf4j
@Component(Constants.ActivityModel.ACTIVITY_SKU_STOCK)
public class ActivitySkuStockChain extends AbstractActivityChain {

    @Resource
    private ActivityService activityService;
    @Resource
    private ActivityRepository repository;

    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.debug("【活动责任链-ActivitySkuStockChain】-suk库存扣减开始 sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        //扣减总库存
        boolean status = activityService.reduceActivitySkuStock(activitySkuEntity.getSku(), activityEntity.getEndDateTime());
        if (!status) {
            log.error("【活动责任链-ActivitySkuStockChain】-suk库存扣减失败");
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(), ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getMessage());
        }
        log.debug("【活动责任链-ActivitySkuStockChain】-suk库存扣减成功");
        // 写入延迟队列，通过redis延迟队列更新数据库
        repository.activitySkuStockConsumeSendQueue(ActivitySkuStockKeyVO.builder()
                .sku(activitySkuEntity.getSku())
                .activityId(activityEntity.getActivityId())
                .build());

        return true;
    }
}

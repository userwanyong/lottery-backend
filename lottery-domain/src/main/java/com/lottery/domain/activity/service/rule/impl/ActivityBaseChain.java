package com.lottery.domain.activity.service.rule.impl;

import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.model.valobj.ActivityStateVO;
import com.lottery.domain.activity.service.rule.AbstractActivityChain;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author 永
 * 活动规则过滤节点【日期、状态、库存】
 */
@Slf4j
@Component(Constants.ActivityModel.ACTIVITY_BASE)
public class ActivityBaseChain extends AbstractActivityChain {
    @Override
    public boolean action(ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity) {
        log.info("【活动责任链】-基础信息【有效期、状态、库存(sku)】校验开始 sku:{} activityId:{}", activitySkuEntity.getSku(), activityEntity.getActivityId());
        if (!ActivityStateVO.open.equals(activityEntity.getState())){
            throw new AppException(ResponseCode.ACTIVITY_STATE_ERROR.getCode(),ResponseCode.ACTIVITY_STATE_ERROR.getMessage());
        }
        //校验日期是否在活动期间
        Date date = new Date();
        if (date.before(activityEntity.getBeginDateTime()) || date.after(activityEntity.getEndDateTime())){
            throw new AppException(ResponseCode.ACTIVITY_DATE_ERROR.getCode(),ResponseCode.ACTIVITY_DATE_ERROR.getMessage());
        }
        //校验是否还存在sku库存
        if (activitySkuEntity.getStockCountSurplus() <= 0){
            throw new AppException(ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getCode(),ResponseCode.ACTIVITY_SKU_STOCK_ERROR.getMessage());
        }
        log.info("【活动责任链】-基础信息【有效期、状态、库存(sku)】校验完成");
        //执行责任链的下一个节点
        return next().action(activitySkuEntity, activityEntity, activityCountEntity);
    }
}

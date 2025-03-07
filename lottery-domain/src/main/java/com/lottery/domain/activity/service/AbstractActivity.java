package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.aggregate.CreateOrderAggregate;
import com.lottery.domain.activity.model.entity.ActivityCountEntity;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.activity.model.entity.ActivitySkuEntity;
import com.lottery.domain.activity.model.entity.SkuRechargeEntity;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.rule.ActivityChain;
import com.lottery.domain.activity.service.rule.factory.DefaultActivityChainFactory;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 永
 * 抽奖活动抽象类，定义标准的流程
 */
@Slf4j
public abstract class AbstractActivity extends ActivitySupport implements ActivityOrder {


    public AbstractActivity(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory) {
        super(activityRepository, defaultActivityChainFactory);
    }

    @Override
    public String createSkuRechargeOrder(SkuRechargeEntity skuRechargeEntity) {
        // 1. 参数校验
        String userId = skuRechargeEntity.getUserId();
        Long sku = skuRechargeEntity.getSku();
        String outBusinessNo = skuRechargeEntity.getOutBusinessNo();
        if (sku == null || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        // 2. 查询基础信息
        // 2.1 查询sku信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        // 2.2 查询活动信息
        ActivityEntity activityEntity = queryActivityByActivityId(activitySkuEntity.getActivityId());
        // 2.3 查询活动次数信息
        ActivityCountEntity activityCountEntity = queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        // 3. 活动动作规则校验 todo 后续处理规则过滤流程，暂时也不处理责任链结果
        ActivityChain activityChain = defaultActivityChainFactory.openActivityChain();
        boolean success = activityChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        // 4. 构建订单聚合对象
        CreateOrderAggregate createOrderAggregate = buildOrderAggregate(skuRechargeEntity, activitySkuEntity, activityEntity, activityCountEntity);

        // 5. 保存订单
        doSaveOrder(createOrderAggregate);

        // 6. 返回单号
        return createOrderAggregate.getActivityOrderEntity().getOrderId();
    }

    protected abstract CreateOrderAggregate buildOrderAggregate(SkuRechargeEntity skuRechargeEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

    protected abstract void doSaveOrder(CreateOrderAggregate createOrderAggregate);

}

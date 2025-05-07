package com.lottery.domain.activity.service.quota;

import com.lottery.domain.activity.model.aggregate.CreateQuotaOrderAggregate;
import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.OrderTradeTypeVO;
import com.lottery.domain.activity.repository.ActivityRepository;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.activity.service.quota.policy.TradePolicy;
import com.lottery.domain.activity.service.quota.rule.ActivityChain;
import com.lottery.domain.activity.service.quota.rule.factory.DefaultActivityChainFactory;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author 永
 * 活动-额度领域-抽象类
 */
@Slf4j
public abstract class AbstractActivityQuota extends ActivitySupportQuota implements ActivityQuotaService {


    private final Map<String, TradePolicy> tradePolicyGroup;

    public AbstractActivityQuota(ActivityRepository activityRepository, DefaultActivityChainFactory defaultActivityChainFactory, Map<String, TradePolicy> tradePolicyGroup) {
        super(activityRepository, defaultActivityChainFactory);
        this.tradePolicyGroup = tradePolicyGroup;
    }

    @Override
    public UnpaidQuotaOrderEntity createQuotaOrder(QuotaOrderEntity quotaOrderEntity) {
        // 1. 参数校验
        String userId = quotaOrderEntity.getUserId();
        Long sku = quotaOrderEntity.getSku();
        String outBusinessNo = quotaOrderEntity.getOutBusinessNo();
        if (sku == null || StringUtils.isBlank(userId) || StringUtils.isBlank(outBusinessNo)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }

        // 2. 查询未支付订单
        UnpaidQuotaOrderEntity unpaidCreditOrder = activityRepository.queryUnpaidQuotaOrder(quotaOrderEntity);
        if (unpaidCreditOrder != null) {
            return unpaidCreditOrder;
        }

        // 3. 查询基础信息
        // 3.1 查询sku表信息
        ActivitySkuEntity activitySkuEntity = queryActivitySku(sku);
        // 3.2 查询活动表信息
        ActivityEntity activityEntity = queryActivityByActivityId(activitySkuEntity.getActivityId());
        // 3.3 查询活动次数表信息
        ActivityCountEntity activityCountEntity = queryActivityCountByActivityCountId(activitySkuEntity.getActivityCountId());

        // 4. 账户额度 【交易属性的兑换，需要校验额度账户】// todo 考虑责任链？
        if (OrderTradeTypeVO.credit_pay_trade.equals(quotaOrderEntity.getOrderTradeTypeVO())){
            BigDecimal availableAmount = activityRepository.queryUserCreditAccountAmount(userId);
            if (availableAmount.compareTo(activitySkuEntity.getProductAmount()) < 0) {
                throw new AppException(ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getCode(), ResponseCode.USER_CREDIT_ACCOUNT_NO_AVAILABLE_AMOUNT.getMessage());
            }
        }

        // 5. 责任链校验
        ActivityChain activityChain = defaultActivityChainFactory.openActivityChain();
        activityChain.action(activitySkuEntity, activityEntity, activityCountEntity);

        // 6. 构建额度单聚合对象
        CreateQuotaOrderAggregate createQuotaOrderAggregate = buildOrderAggregate(quotaOrderEntity, activitySkuEntity, activityEntity, activityCountEntity);

        // 7. 保存额度单
        TradePolicy tradePolicy = tradePolicyGroup.get(quotaOrderEntity.getOrderTradeTypeVO().getCode());
        tradePolicy.trade(createQuotaOrderAggregate);

        // 8. 返回额度单实体
        ActivityOrderEntity activityOrderEntity = createQuotaOrderAggregate.getActivityOrderEntity();
        return UnpaidQuotaOrderEntity.builder()
                .userId(activityOrderEntity.getUserId())
                .orderId(activityOrderEntity.getOrderId())
                .outBusinessNo(activityOrderEntity.getOutBusinessNo())
                .payAmount(activityOrderEntity.getPayAmount())
                .build();
    }

    protected abstract CreateQuotaOrderAggregate buildOrderAggregate(QuotaOrderEntity quotaOrderEntity, ActivitySkuEntity activitySkuEntity, ActivityEntity activityEntity, ActivityCountEntity activityCountEntity);

}

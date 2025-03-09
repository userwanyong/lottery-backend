package com.lottery.domain.activity.service;

import com.lottery.domain.activity.model.entity.SkuRechargeEntity;

/**
 * @author 永
 * 额度领域-活动服务接口
 */
public interface ActivityQuotaService {
    /**
     * 创建 sku 账户充值订单，给用户增加抽奖次数
     *
     * @param skuRechargeEntity 活动商品充值实体对象
     * @return 活动ID
     */
    String createQuotaOrder(SkuRechargeEntity skuRechargeEntity);

}

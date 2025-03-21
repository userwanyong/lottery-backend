package com.lottery.domain.rebate.service;

import com.lottery.domain.rebate.model.entity.BehaviorEntity;

import java.util.List;

/**
 * @author 永
 * 返利领域接口
 */
public interface RebateService {
    List<String> createRebateOrder(BehaviorEntity behaviorEntity);
}

package com.lottery.domain.rebate.repository;

import com.lottery.domain.rebate.model.aggregate.RebateAggregate;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.model.valobj.RebateVO;

import java.util.List;

/**
 * @author 永
 * 返利领域仓储接口
 */
public interface RebateRepository {

    List<RebateVO> queryRebateConfig(BehaviorEntity behaviorEntity);

    void saveRebateAggregate(List<RebateAggregate> aggregates);

    List<RebateOrderEntity> queryRebateOrder(String userId, String outBusinessNo);

    boolean queryIsHaveRebateOrder(String userId,Long activityId, String outBusinessNo);
}

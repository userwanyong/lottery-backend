package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.BehaviorRebateRequestDTO;
import com.lottery.trigger.api.dto.res.BehaviorRebateResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface BehaviorRebateService {
    BaseResponse<List<BehaviorRebateResponseDTO>> queryBehaviorRebate();

    BaseResponse<Boolean> addBehaviorRebate(BehaviorRebateRequestDTO request);

    BaseResponse<Boolean> updateBehaviorRebate(BehaviorRebateRequestDTO request);

    BaseResponse<Boolean> deleteBehaviorRebate(Long behaviorRebateId);
}

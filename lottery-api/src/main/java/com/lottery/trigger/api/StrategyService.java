package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.StrategyRequestDTO;
import com.lottery.trigger.api.dto.res.StrategyResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface StrategyService {
    BaseResponse<List<StrategyResponseDTO>> queryStrategy();

    BaseResponse<Boolean> addStrategy(StrategyRequestDTO request);

    BaseResponse<Boolean> updateStrategy(StrategyRequestDTO request);

    BaseResponse<Boolean> deleteStrategy(Long strategyId);
}

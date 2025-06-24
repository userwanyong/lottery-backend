package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.StrategyAwardRequestDTO;
import com.lottery.trigger.api.dto.res.StrategyAwardResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface StrategyAwardService {
    BaseResponse<List<StrategyAwardResponseDTO>> queryStrategyAward();
    BaseResponse<Boolean> addStrategyAward(StrategyAwardRequestDTO request);
    BaseResponse<Boolean> updateStrategyAward(StrategyAwardRequestDTO request);
    BaseResponse<Boolean> deleteStrategyAward(Long strategyAwardId);
}

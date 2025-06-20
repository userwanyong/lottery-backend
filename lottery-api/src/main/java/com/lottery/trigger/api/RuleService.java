package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.RuleRequestDTO;
import com.lottery.trigger.api.dto.res.RuleResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface RuleService {
    BaseResponse<List<RuleResponseDTO>> queryRule();

    BaseResponse<Boolean> addRule(RuleRequestDTO request);

    BaseResponse<Boolean> updateRule(RuleRequestDTO request);

    BaseResponse<Boolean> deleteRule(Long ruleId);
}

package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.RuleTreeNodeLineRequestDTO;
import com.lottery.trigger.api.dto.res.RuleTreeNodeLineResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface RuleTreeNodeLineService {
    BaseResponse<List<RuleTreeNodeLineResponseDTO>> queryRuleTreeNodeLine();
    BaseResponse<Boolean> addRuleTreeNodeLine(RuleTreeNodeLineRequestDTO request);
    BaseResponse<Boolean> updateRuleTreeNodeLine(RuleTreeNodeLineRequestDTO request);
    BaseResponse<Boolean> deleteRuleTreeNodeLine(Long ruleTreeNodeLineId);
}

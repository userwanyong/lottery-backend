package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.RuleTreeRequestDTO;
import com.lottery.trigger.api.dto.res.RuleTreeResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 * 奖品规则
 */
public interface RuleTreeService {
    BaseResponse<List<RuleTreeResponseDTO>> queryRuleTree();
    BaseResponse<Boolean> addRuleTree(RuleTreeRequestDTO request);
    BaseResponse<Boolean> updateRuleTree(RuleTreeRequestDTO request);
    BaseResponse<Boolean> deleteRuleTree(Long ruleTreeId);
}

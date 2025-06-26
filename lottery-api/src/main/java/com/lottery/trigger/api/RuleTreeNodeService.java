package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.RuleTreeNodeRequestDTO;
import com.lottery.trigger.api.dto.res.RuleTreeNodeResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface RuleTreeNodeService {
    BaseResponse<List<RuleTreeNodeResponseDTO>> queryRuleTreeNode();
    BaseResponse<Boolean> addRuleTreeNode(RuleTreeNodeRequestDTO request);
    BaseResponse<Boolean> updateRuleTreeNode(RuleTreeNodeRequestDTO request);
    BaseResponse<Boolean> deleteRuleTreeNode(Long ruleTreeNodeId);
}

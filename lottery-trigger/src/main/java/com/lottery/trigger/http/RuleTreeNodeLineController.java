package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.RuleTreeNodeLineVO;
import com.lottery.trigger.api.RuleTreeNodeLineService;
import com.lottery.trigger.api.dto.req.RuleTreeNodeLineRequestDTO;
import com.lottery.trigger.api.dto.res.RuleTreeNodeLineResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/erp/rule/tree/node/line")
public class RuleTreeNodeLineController implements RuleTreeNodeLineService {
    @Resource
    private ErpRepository repository;

    @Override
    @GetMapping("/query_rule_tree_node_line")
    public BaseResponse<List<RuleTreeNodeLineResponseDTO>> queryRuleTreeNodeLine() {
        log.info("======================[ErpOperateController-queryRuleTreeNodeLine]运营端 查询规则树节点连线开始 ======================");
        List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = repository.queryRuleTreeNodeLineVO();
        ArrayList<RuleTreeNodeLineResponseDTO> list = new ArrayList<>();
        for (RuleTreeNodeLineVO ruleTreeNodeLineVO : ruleTreeNodeLineVOList) {
            RuleTreeNodeLineResponseDTO ruleTreeNodeLineResponseDTO = new RuleTreeNodeLineResponseDTO();
            BeanUtils.copyProperties(ruleTreeNodeLineVO, ruleTreeNodeLineResponseDTO);
            list.add(ruleTreeNodeLineResponseDTO);
        }
        log.info("======================[ErpOperateController-queryRuleTreeNodeLine]运营端 获取规则树节点连线成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_rule_tree_node_line")
    public BaseResponse<Boolean> addRuleTreeNodeLine(@RequestBody RuleTreeNodeLineRequestDTO request) {
        log.info("======================[ErpOperateController-add]运营端 添加规则树节点连线开始 ======================");
        if (request.getRuleTreeId() == null || StringUtils.isBlank(request.getRuleNodeFrom()) ||
                StringUtils.isBlank(request.getRuleNodeTo())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        RuleTreeNodeLineVO ruleTreeNodeLineVO = new RuleTreeNodeLineVO();
        BeanUtils.copyProperties(request, ruleTreeNodeLineVO);
        repository.addRuleTreeNodeLineVO(ruleTreeNodeLineVO);
        log.info("======================[ErpOperateController-add]运营端 添加规则树节点连线成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/update_rule_tree_node_line")
    public BaseResponse<Boolean> updateRuleTreeNodeLine(@RequestBody RuleTreeNodeLineRequestDTO request) {
        log.info("======================[ErpOperateController-update]运营端 修改规则树节点连线开始 ======================");
        if (request.getId() == null || request.getRuleTreeId() == null || StringUtils.isBlank(request.getRuleNodeFrom()) ||
                StringUtils.isBlank(request.getRuleNodeTo())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        RuleTreeNodeLineVO ruleTreeNodeLineVO = new RuleTreeNodeLineVO();
        BeanUtils.copyProperties(request, ruleTreeNodeLineVO);
        repository.updateRuleTreeNodeLineVO(ruleTreeNodeLineVO);
        log.info("======================[ErpOperateController-update]运营端 修改规则树节点连线成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/delete_rule_tree_node_line/{ruleTreeNodeLineId}")
    public BaseResponse<Boolean> deleteRuleTreeNodeLine(@PathVariable("ruleTreeNodeLineId") Long ruleTreeNodeLineId) {
        log.info("======================[ErpOperateController-delete]运营端 删除规则树节点连线开始 ======================");
        repository.deleteRuleTreeNodeLineVO(ruleTreeNodeLineId);
        log.info("======================[ErpOperateController-delete]运营端 删除规则树节点连线成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }
}

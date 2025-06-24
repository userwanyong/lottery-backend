package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.RuleTreeVO;
import com.lottery.trigger.api.RuleTreeService;
import com.lottery.trigger.api.dto.req.RuleTreeRequestDTO;
import com.lottery.trigger.api.dto.res.RuleTreeResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
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
@RequestMapping("/erp/rule/tree")
public class RuleTreeController implements RuleTreeService {
    @Resource
    private ErpRepository repository;
    @Override
    @GetMapping("/query_rule_tree")
    public BaseResponse<List<RuleTreeResponseDTO>> queryRuleTree() {
        log.info("======================[ErpOperateController-queryRuleTree]运营端 查询奖品规则开始 ======================");
        List<RuleTreeVO> ruleTrees = repository.queryRuleTreeVOList();
        ArrayList<RuleTreeResponseDTO> list = new ArrayList<>();
        for (RuleTreeVO ruleTreeVO : ruleTrees) {
            RuleTreeResponseDTO ruleTreeResponseDTO = new RuleTreeResponseDTO();
            BeanUtils.copyProperties(ruleTreeVO, ruleTreeResponseDTO);
            list.add(ruleTreeResponseDTO);
        }
        log.info("======================[ErpOperateController-queryRuleTree]运营端 查询奖品规则成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_rule_tree")
    public BaseResponse<Boolean> addRuleTree(@RequestBody RuleTreeRequestDTO request) {
        return null;
    }

    @Override
    @PostMapping("/update_rule_tree")
    public BaseResponse<Boolean> updateRuleTree(@RequestBody RuleTreeRequestDTO request) {
        return null;
    }

    @Override
    @PostMapping("/delete_rule_tree/{ruleTreeId}")
    public BaseResponse<Boolean> deleteRuleTree(@PathVariable("ruleTreeId") Long ruleTreeId) {
        return null;
    }
}

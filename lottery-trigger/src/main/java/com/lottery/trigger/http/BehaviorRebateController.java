package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.BehaviorRebateVO;
import com.lottery.trigger.api.BehaviorRebateService;
import com.lottery.trigger.api.dto.req.BehaviorRebateRequestDTO;
import com.lottery.trigger.api.dto.res.BehaviorRebateResponseDTO;
import com.lottery.types.annotation.PermissionCheck;
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
@CrossOrigin("*")
@RequestMapping("/erp/behavior")
public class BehaviorRebateController implements BehaviorRebateService {
    @Resource
    private ErpRepository repository;
    @Override
    @GetMapping("/query_behavior")
    public BaseResponse<List<BehaviorRebateResponseDTO>> queryBehaviorRebate() {
        log.info("======================[BehaviorRebateController-queryBehaviorRebate]运营端 查询返利配置开始 ======================");
        List<BehaviorRebateVO> behaviorRebates = repository.queryBehaviorRebateVOList();
        ArrayList<BehaviorRebateResponseDTO> list = new ArrayList<>();
        for (BehaviorRebateVO behaviorRebateVO : behaviorRebates) {
            BehaviorRebateResponseDTO behaviorRebateResponseDTO = new BehaviorRebateResponseDTO();
            BeanUtils.copyProperties(behaviorRebateVO, behaviorRebateResponseDTO);
            list.add(behaviorRebateResponseDTO);
        }
        log.info("======================[BehaviorRebateController-queryBehaviorRebate]运营端 查询返利配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @GetMapping("/query_behavior_gift")
    public BaseResponse<List<BehaviorRebateResponseDTO>> queryBehaviorRebateOfGift(@RequestParam Long activityId) {
        log.info("======================[BehaviorRebateController-queryBehaviorRebate]运营端 查询赠送的返利配置开始 ======================");
        List<BehaviorRebateVO> behaviorRebates = repository.queryBehaviorRebateVOListOfGift(activityId);
        ArrayList<BehaviorRebateResponseDTO> list = new ArrayList<>();
        for (BehaviorRebateVO behaviorRebateVO : behaviorRebates) {
            BehaviorRebateResponseDTO behaviorRebateResponseDTO = new BehaviorRebateResponseDTO();
            BeanUtils.copyProperties(behaviorRebateVO, behaviorRebateResponseDTO);
            list.add(behaviorRebateResponseDTO);
        }
        log.info("======================[BehaviorRebateController-queryBehaviorRebate]运营端 查询赠送的返利配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_behavior")
    @PermissionCheck(roles = {0})
    public BaseResponse<Boolean> addBehaviorRebate(@RequestBody BehaviorRebateRequestDTO request) {
        log.info("======================[BehaviorRebateController-addBehaviorRebate]运营端 添加返利配置开始 ======================");
        BehaviorRebateVO behaviorRebateVO = new BehaviorRebateVO();
        BeanUtils.copyProperties(request, behaviorRebateVO);
        repository.addBehaviorRebateVO(behaviorRebateVO);
        log.info("======================[BehaviorRebateController-addBehaviorRebate]运营端 添加返利配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/update_behavior")
    @PermissionCheck(roles = {0})
    public BaseResponse<Boolean> updateBehaviorRebate(@RequestBody BehaviorRebateRequestDTO request) {
        log.info("======================[BehaviorRebateController-updateBehaviorRebate]运营端 修改返利配置开始 ======================");
        BehaviorRebateVO behaviorRebateVO = new BehaviorRebateVO();
        BeanUtils.copyProperties(request, behaviorRebateVO);
        repository.updateBehaviorRebateVO(behaviorRebateVO);
        log.info("======================[BehaviorRebateController-updateBehaviorRebate]运营端 添加返利配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/delete_behavior/{behaviorRebateId}")
    @PermissionCheck(roles = {0})
    public BaseResponse<Boolean> deleteBehaviorRebate(@PathVariable("behaviorRebateId") Long behaviorRebateId) {
        log.info("======================[BehaviorRebateController-deleteBehaviorRebate]运营端 删除返利配置开始 ======================");
        repository.deleteBehaviorRebateVO(behaviorRebateId);
        log.info("======================[BehaviorRebateController-deleteBehaviorRebate]运营端 删除返利配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }
}

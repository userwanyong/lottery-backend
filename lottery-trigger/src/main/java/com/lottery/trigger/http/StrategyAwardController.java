package com.lottery.trigger.http;


import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.StrategyAwardVO;
import com.lottery.trigger.api.StrategyAwardService;
import com.lottery.trigger.api.dto.req.StrategyAwardRequestDTO;
import com.lottery.trigger.api.dto.res.StrategyAwardResponseDTO;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync;
import com.lottery.types.annotation.PermissionCheck;
import com.lottery.types.common.Constants;
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
@CrossOrigin("*")
@RequestMapping("/erp/strategy/award")
public class StrategyAwardController implements StrategyAwardService {
    @Resource
    private ErpRepository repository;
    @Resource
    private StrategyArmory strategyArmory;

    @Override
    @GetMapping("/query_strategy_award")
    public BaseResponse<List<StrategyAwardResponseDTO>> queryStrategyAward() {
        log.info("======================[ErpOperateController-queryStrategyAward]运营端 查询策略奖品开始 ======================");
        List<StrategyAwardVO> strategyAwards = repository.queryStrategyAwardVOList();
        ArrayList<StrategyAwardResponseDTO> list = new ArrayList<>();
        for (StrategyAwardVO strategyAwardVO : strategyAwards) {
            StrategyAwardResponseDTO strategyAwardResponseDTO = new StrategyAwardResponseDTO();
            BeanUtils.copyProperties(strategyAwardVO, strategyAwardResponseDTO);
            list.add(strategyAwardResponseDTO);
        }
        log.info("======================[ErpOperateController-queryStrategyAward]运营端 策略奖品查询成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_strategy_award")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY,Constants.RedisKey.ACTIVITY_AWARD_COUNT_KEY,Constants.RedisKey.ACTIVITY_AWARD_KEY})
    public BaseResponse<Boolean> addStrategyAward(@RequestBody StrategyAwardRequestDTO request) {
        log.info("======================[ErpOperateController-addStrategyAward]运营端 添加策略奖品开始 ======================");
        if (StringUtils.isBlank(request.getAwardTitle()) || request.getAwardCount() == null ||
                request.getAwardCountSurplus() == null || request.getAwardRate() == null ||
                request.getActivityId() == null || request.getAwardId() == null ||
                request.getSort() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        StrategyAwardVO strategyAwardVO = new StrategyAwardVO();
        BeanUtils.copyProperties(request, strategyAwardVO);
        repository.addStrategyAwardVO(strategyAwardVO);
        strategyArmory.assembleLotteryStrategyByActivityId(request.getActivityId());
        log.info("======================[ErpOperateController-addStrategyAward]运营端 添加策略奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/update_strategy_award")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY,Constants.RedisKey.ACTIVITY_AWARD_COUNT_KEY,Constants.RedisKey.ACTIVITY_AWARD_KEY})
    public BaseResponse<Boolean> updateStrategyAward(@RequestBody StrategyAwardRequestDTO request) {
        log.info("======================[ErpOperateController-updateStrategyAward]运营端 修改策略奖品开始 ======================");
        if (StringUtils.isBlank(request.getAwardTitle()) || request.getAwardCount() == null ||
                request.getAwardCountSurplus() == null || request.getAwardRate() == null ||
                request.getActivityId() == null || request.getAwardId() == null ||
                request.getSort() == null || request.getId() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        StrategyAwardVO strategyAwardVO = new StrategyAwardVO();
        BeanUtils.copyProperties(request, strategyAwardVO);
        repository.updateStrategyAwardVO(strategyAwardVO);
        strategyArmory.assembleLotteryStrategyByActivityId(request.getActivityId());
        log.info("======================[ErpOperateController-updateStrategyAward]运营端 修改策略奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/delete_strategy_award/{strategyAwardId}")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY,Constants.RedisKey.ACTIVITY_AWARD_COUNT_KEY,Constants.RedisKey.ACTIVITY_AWARD_KEY})
    public BaseResponse<Boolean> deleteStrategyAward(@PathVariable("strategyAwardId") Long strategyAwardId) {
        log.info("======================[ErpOperateController-deleteStrategyAward]运营端 删除策略奖品开始 ======================");
        repository.deleteStrategyAwardVO(strategyAwardId);
        log.info("======================[ErpOperateController-deleteStrategyAward]运营端 删除策略奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}

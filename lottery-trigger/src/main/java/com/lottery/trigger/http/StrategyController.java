package com.lottery.trigger.http;


import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.StrategyVO;
import com.lottery.trigger.api.StrategyService;
import com.lottery.trigger.api.dto.req.StrategyRequestDTO;
import com.lottery.trigger.api.dto.res.StrategyResponseDTO;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixSync;
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
@CrossOrigin
@RequestMapping("/erp/strategy")
public class StrategyController implements StrategyService {
    @Resource
    private ErpRepository repository;
    @Resource
    private StrategyArmory strategyArmory;

    @Override
    @GetMapping("/query_strategy")
    public BaseResponse<List<StrategyResponseDTO>> queryStrategy() {
        log.info("======================[StrategyController-queryStrategy]运营端 查询策略开始 ======================");
        List<StrategyVO> strategys = repository.queryStrategyVOList();
        ArrayList<StrategyResponseDTO> list = new ArrayList<>();
        for (StrategyVO strategyVO : strategys) {
            StrategyResponseDTO strategyResponseDTO = new StrategyResponseDTO();
            BeanUtils.copyProperties(strategyVO, strategyResponseDTO);
            list.add(strategyResponseDTO);
        }
        log.info("======================[StrategyController-queryStrategy]运营端 策略列表查询成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_strategy")
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_KEY})
    @DeleteOldCacheWithPrefixSync(key = {Constants.RedisKey.RATE_RANGE_KEY,Constants.RedisKey.RATE_TABLE_KEY},keyParam = "#request.id")
    public BaseResponse<Boolean> addStrategy(@RequestBody StrategyRequestDTO request) {
        log.info("======================[StrategyController-addStrategy]运营端 添加策略开始 ======================");
        // 1. 参数校验
        if (StringUtils.isBlank(request.getStrategyDesc())) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        StrategyVO strategyVO = new StrategyVO();
        BeanUtils.copyProperties(request, strategyVO);
        repository.addStrategyVO(strategyVO);
        log.info("======================[StrategyController-addStrategy]运营端 添加策略成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/update_strategy")
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_KEY})
    @DeleteOldCacheWithPrefixSync(key = {Constants.RedisKey.RATE_RANGE_KEY,Constants.RedisKey.RATE_TABLE_KEY},keyParam = "#request.id")
    public BaseResponse<Boolean> updateStrategy(@RequestBody StrategyRequestDTO request) {
        log.info("======================[StrategyController-updateStrategy]运营端 修改策略开始 ======================");
        // 1. 参数校验
        if (StringUtils.isBlank(request.getStrategyDesc())  || request.getId() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        StrategyVO strategyVO = new StrategyVO();
        BeanUtils.copyProperties(request, strategyVO);
        repository.updateStrategyVO(strategyVO);
        // 2. 装配抽奖算法
        strategyArmory.assembleLotteryStrategy(request.getId());
        log.info("[StrategyController-updateStrategy]装配抽奖算法成功");
        log.info("======================[StrategyController-updateStrategy]运营端 修改策略成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/delete_strategy/{strategyId}")
//    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_KEY})
//    @DeleteOldCacheWithPrefixSync(key = {Constants.RedisKey.RATE_RANGE_KEY,Constants.RedisKey.RATE_TABLE_KEY},keyParam = "#request.id")
    public BaseResponse<Boolean> deleteStrategy(@PathVariable("strategyId") Long strategyId) {
        log.info("======================[StrategyController-deleteStrategy]运营端 删除策略开始 ======================");
        repository.deleteStrategyVO(strategyId);
        log.info("======================[StrategyController-deleteStrategy]运营端 删除策略成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}

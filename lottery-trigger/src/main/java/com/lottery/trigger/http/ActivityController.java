package com.lottery.trigger.http;

import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.ActivityVO;
import com.lottery.trigger.api.ActivityService;
import com.lottery.trigger.api.dto.req.ActivityRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityResponseDTO;
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
@RequestMapping("/erp/activity")
public class ActivityController implements ActivityService {
    @Resource
    private ErpRepository repository;
    @Resource
    private StrategyArmory strategyArmory;

    @Override
    @GetMapping("/query_activity")
    public BaseResponse<List<ActivityResponseDTO>> queryActivity() {
        log.info("======================[ActivityController-queryActivity]运营端 查询活动开始 ======================");
        List<ActivityVO> activitys = repository.queryActivityVOList();
        ArrayList<ActivityResponseDTO> list = new ArrayList<>();
        for (ActivityVO activityVO : activitys) {
            ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO();
            BeanUtils.copyProperties(activityVO, activityResponseDTO);
            list.add(activityResponseDTO);
        }
        log.info("======================[ActivityController-queryActivity]运营端 查询活动成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_activity")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_KEY)
    public BaseResponse<Boolean> addActivity(@RequestBody ActivityRequestDTO request) {
        log.info("======================[ActivityController-add]运营端 添加活动开始 ======================");
        // 1. 参数校验
        if (StringUtils.isBlank(request.getActivityName()) || StringUtils.isBlank(request.getActivityDesc()) ||
                request.getStrategyId() == null || request.getBeginDateTime() == null ||
                request.getEndDateTime() == null || request.getState() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(request, activityVO);
        repository.addActivityVO(activityVO);
        // 2. 装配抽奖算法
        strategyArmory.assembleLotteryStrategy(request.getStrategyId());
        log.info("[ActivityController-add]装配抽奖算法成功");
        log.info("======================[ActivityController-add]运营端 添加活动成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/update_activity")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_KEY)
    public BaseResponse<Boolean> updateActivity(@RequestBody ActivityRequestDTO request) {
        log.info("======================[ActivityController-update]运营端 修改活动开始 ======================");
        // 1. 参数校验
        if (StringUtils.isBlank(request.getActivityName()) || StringUtils.isBlank(request.getActivityDesc()) ||
                request.getStrategyId() == null || request.getBeginDateTime() == null ||
                request.getEndDateTime() == null || request.getState() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(request, activityVO);
        repository.updateActivityVO(activityVO);
        log.info("======================[ActivityController-update]运营端 修改活动成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/delete_activity/{activityId}")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_KEY)
    public BaseResponse<Boolean> deleteActivity(@PathVariable("activityId") Long activityId) {
        log.info("======================[ActivityController-delete]运营端 删除活动开始 ======================");
        repository.deleteActivityVO(activityId);
        log.info("======================[ActivityController-delete]运营端 删除活动成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}

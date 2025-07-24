package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.ActivityCountVO;
import com.lottery.trigger.api.ActivityCountService;
import com.lottery.trigger.api.dto.req.ActivityCountRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityCountResponseDTO;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
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
@RequestMapping("/erp/activity_count")
public class ActivityCountController implements ActivityCountService {
    @Resource
    private ErpRepository repository;

    @Override
    @GetMapping("/query_activity_count")
    public BaseResponse<List<ActivityCountResponseDTO>> queryActivityCount() {
        log.info("======================[ActivityCountController-queryActivityCount]运营端 查询活动次数配置开始 ======================");
        List<ActivityCountVO> activityCounts = repository.queryActivityCountVOList();
        ArrayList<ActivityCountResponseDTO> list = new ArrayList<>();
        for (ActivityCountVO activityCountVO : activityCounts) {
            ActivityCountResponseDTO activityCountResponseDTO = new ActivityCountResponseDTO();
            BeanUtils.copyProperties(activityCountVO, activityCountResponseDTO);
            list.add(activityCountResponseDTO);
        }
        log.info("======================[ActivityCountController-queryActivityCount]运营端 查询活动次数配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_activity_count")
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_COUNT_KEY)
    public BaseResponse<Boolean> addActivityCount(@RequestBody ActivityCountRequestDTO request) {
        // 1. 参数校验
        if (request.getDayCount() == null || request.getMonthCount() == null || request.getTotalCount() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        log.info("======================[ActivityCountController-addActivityCount]运营端 添加活动次数配置开始 ======================");
        ActivityCountVO activityCountVO = new ActivityCountVO();
        BeanUtils.copyProperties(request, activityCountVO);
        repository.addActivityCountVO(activityCountVO);
        log.info("======================[ActivityCountController-addActivityCount]运营端 添加活动次数配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/update_activity_count")
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_COUNT_KEY)
    public BaseResponse<Boolean> updateActivityCount(@RequestBody ActivityCountRequestDTO request) {
        // 1. 参数校验
        if (request.getDayCount() == null || request.getMonthCount() == null || request.getTotalCount() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        log.info("======================[ActivityCountController-updateActivityCount]运营端 修改活动次数配置开始 ======================");
        ActivityCountVO activityCountVO = new ActivityCountVO();
        BeanUtils.copyProperties(request, activityCountVO);
        repository.updateActivityCountVO(activityCountVO);
        log.info("======================[ActivityCountController-updateActivityCount]运营端 添加活动次数配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/delete_activity_count/{activityCountId}")
    @DeleteOldCacheWithPrefixAsync(key = Constants.RedisKey.ACTIVITY_COUNT_KEY)
    public BaseResponse<Boolean> deleteActivityCount(@PathVariable("activityCountId") Long activityCountId) {
        log.info("======================[ActivityCountController-deleteActivityCount]运营端 删除活动次数配置开始 ======================");
        repository.deleteActivityCountVO(activityCountId);
        log.info("======================[ActivityCountController-deleteActivityCount]运营端 删除活动次数配置成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}

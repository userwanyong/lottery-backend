package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.ActivityCountRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityCountResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface ActivityCountService {
    BaseResponse<List<ActivityCountResponseDTO>> queryActivityCount();
    BaseResponse<Boolean> addActivityCount(ActivityCountRequestDTO request);
    BaseResponse<Boolean> updateActivityCount(ActivityCountRequestDTO request);
    BaseResponse<Boolean> deleteActivityCount(Long activityCountId);
}

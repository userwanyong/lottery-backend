package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.ActivityRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface ActivityService {
    BaseResponse<List<ActivityResponseDTO>> queryActivity();
    BaseResponse<Boolean> addActivity(ActivityRequestDTO request);
    BaseResponse<Boolean> updateActivity(ActivityRequestDTO request);
    BaseResponse<Boolean> deleteActivity(Long activityId);
}

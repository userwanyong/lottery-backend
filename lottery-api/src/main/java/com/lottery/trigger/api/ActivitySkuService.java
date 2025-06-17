package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.ActivitySkuRequestDTO;
import com.lottery.trigger.api.dto.res.ActivitySkuResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface ActivitySkuService {
    BaseResponse<List<ActivitySkuResponseDTO>> queryActivitySku();

    BaseResponse<Boolean> addActivitySku(ActivitySkuRequestDTO request);

    BaseResponse<Boolean> updateActivitySku(ActivitySkuRequestDTO request);

    BaseResponse<Boolean> deleteActivitySku(Long activitySkuId);
}

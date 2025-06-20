package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.AwardRequestDTO;
import com.lottery.trigger.api.dto.res.AwardResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 */
public interface AwardService {
    BaseResponse<List<AwardResponseDTO>> queryAward();
    BaseResponse<Boolean> addAward(AwardRequestDTO request);
    BaseResponse<Boolean> updateAward(AwardRequestDTO request);
    BaseResponse<Boolean> deleteAward(Long awardId);
}

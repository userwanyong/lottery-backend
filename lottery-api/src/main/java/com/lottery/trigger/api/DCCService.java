package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.req.DCCRequestDTO;
import com.lottery.types.model.BaseResponse;

/**
 * @author 永
 * DCC 动态配置中心
 */
public interface DCCService {
    BaseResponse<Boolean> updateConfig(DCCRequestDTO requestDTO);
    BaseResponse<String> queryConfig(String key);
}

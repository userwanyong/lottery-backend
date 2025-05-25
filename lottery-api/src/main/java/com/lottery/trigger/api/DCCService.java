package com.lottery.trigger.api;


import com.lottery.types.model.BaseResponse;

/**
 * @author 永
 * DCC 动态配置中心
 */
public interface DCCService {
    BaseResponse<Boolean> updateConfig(String key, String value);
}

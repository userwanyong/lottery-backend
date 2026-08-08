package com.lottery.trigger.http;


import com.lottery.trigger.api.DCCService;
import com.lottery.trigger.api.dto.req.DCCRequestDTO;
import com.lottery.trigger.config.ConfigService;
import com.lottery.types.annotation.PermissionCheck;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 永
 * degradeSwitch:close 抽奖是否降级 默认关闭
 * rateLimiterSwitch:open 抽奖是否进行限流 默认开启
 * 轻量版：配置存储于数据库 sys_config 表，去 Zookeeper
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/dcc")
public class DCCController implements DCCService {
    @Resource
    private ConfigService configService;

    @Override
    @PostMapping("/update_config")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    public BaseResponse<Boolean> updateConfig(@RequestBody DCCRequestDTO requestDTO) {
        String key = requestDTO.getKey();
        String value = requestDTO.getValue();
        try {
            log.info("DCC 动态配置值变更开始 key:{} value:{}", key, value);
            configService.update(key, value);
            log.info("DCC 动态配置值变更完成 key:{} value:{}", key, value);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
        } catch (Exception e) {
            log.info("DCC 动态配置值变更失败 key:{} value:{}", key, value, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_config")
    public BaseResponse<String> queryConfig(@RequestParam String key) {
        try {
            String value = configService.get(key);
            log.info("DCC 获取动态配置值成功 key:{} value:{}", key, value);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), value);
        } catch (Exception e) {
            log.info("DCC 获取动态配置值失败 key:{}", key, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }
}

package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.res.*;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 * 运营后台数据接口
 */
public interface ErpOperateService {
    BaseResponse<List<EsUserOrderResponseDTO>> queryUserOrder();
    BaseResponse<List<EsActivityAccountResponseDTO>> queryActivityAccount();
    BaseResponse<List<EsUserAwardRecordResponseDTO>> queryUserAwardRecord();
    BaseResponse<List<EsCreditAccountResponseDTO>> queryCreditAccount();
    BaseResponse<List<EsCreditRecordResponseDTO>> queryCreditRecord();
    BaseResponse<List<EsActivityRecordResponseDTO>> queryActivityRecord();
    BaseResponse<List<EsUserBehaviorRebateOrderResponseDTO>> queryUserBehaviorRebateOrder();
}

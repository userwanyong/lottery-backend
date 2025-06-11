package com.lottery.trigger.api;


import com.lottery.trigger.api.dto.res.EsActivityAccountResponseDTO;
import com.lottery.trigger.api.dto.res.EsUserOrderResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.util.List;

/**
 * @author 永
 * 运营后台数据接口
 */
public interface ErpOperateService {
    BaseResponse<List<EsUserOrderResponseDTO>> queryUserOrder();
    BaseResponse<List<EsActivityAccountResponseDTO>> queryActivityAccount();
}

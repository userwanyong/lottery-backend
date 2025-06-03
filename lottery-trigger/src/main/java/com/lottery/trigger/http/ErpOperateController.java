package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.EsUserOrderRepository;
import com.lottery.querys.model.valobj.EsUserOrderVO;
import com.lottery.trigger.api.ErpOperateService;
import com.lottery.trigger.api.dto.res.EsUserOrderResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/erp")
public class ErpOperateController implements ErpOperateService {
    @Resource
    private EsUserOrderRepository repository;

    @Override
    @GetMapping("/query_user_order")
    public BaseResponse<List<EsUserOrderResponseDTO>> queryUserOrder() {
        try {
            log.info("======================[ErpOperateController-queryUserOrder]运营端 查询用户抽奖单开始 ======================");
            List<EsUserOrderVO> esUserOrders = repository.queryEsUserOrderVOList();
            ArrayList<EsUserOrderResponseDTO> list = new ArrayList<>();
            for (EsUserOrderVO esUserOrderVO : esUserOrders) {
                EsUserOrderResponseDTO esUserOrderResponseDTO = new EsUserOrderResponseDTO();
                BeanUtils.copyProperties(esUserOrderVO, esUserOrderResponseDTO);
                list.add(esUserOrderResponseDTO);
            }
            log.info("======================[ErpOperateController-armory]运营端 查询用户抽奖单成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryUserOrder]运营端 获取用户抽奖单失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }
}

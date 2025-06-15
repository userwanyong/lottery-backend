package com.lottery.trigger.http;


import com.lottery.querys.adapter.repository.EsErpRepository;
import com.lottery.querys.model.valobj.*;
import com.lottery.trigger.api.ErpOperateService;
import com.lottery.trigger.api.dto.res.*;
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
    private EsErpRepository repository;

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
            log.info("======================[ErpOperateController-queryUserOrder]运营端 查询用户抽奖单成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryUserOrder]运营端 获取用户抽奖单失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_activity_account")
    public BaseResponse<List<EsActivityAccountResponseDTO>> queryActivityAccount() {
        try {
            log.info("======================[ErpOperateController-queryActivityAccount]运营端 查询活动账户开始 ======================");
            List<EsActivityAccountVO> esActivityAccounts = repository.queryEsActivityAccountVOList();
            ArrayList<EsActivityAccountResponseDTO> list = new ArrayList<>();
            for (EsActivityAccountVO esActivityAccountVO : esActivityAccounts) {
                EsActivityAccountResponseDTO esActivityAccountResponseDTO = new EsActivityAccountResponseDTO();
                BeanUtils.copyProperties(esActivityAccountVO, esActivityAccountResponseDTO);
                list.add(esActivityAccountResponseDTO);
            }
            log.info("======================[ErpOperateController-queryActivityAccount]运营端 查询活动账户成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryActivityAccount]运营端 查询活动账户失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_user_award_record")
    public BaseResponse<List<EsUserAwardRecordResponseDTO>> queryUserAwardRecord() {
        try {
            log.info("======================[ErpOperateController-queryUserAwardRecord]运营端 查询中奖记录开始 ======================");
            List<EsUserAwardRecordVO> esUserAwardRecords = repository.queryEsUserAwardRecordVOList();
            ArrayList<EsUserAwardRecordResponseDTO> list = new ArrayList<>();
            for (EsUserAwardRecordVO esUserAwardRecord : esUserAwardRecords) {
                EsUserAwardRecordResponseDTO esUserAwardRecordResponseDTO = new EsUserAwardRecordResponseDTO();
                BeanUtils.copyProperties(esUserAwardRecord, esUserAwardRecordResponseDTO);
                list.add(esUserAwardRecordResponseDTO);
            }
            log.info("======================[ErpOperateController-queryUserAwardRecord]运营端 查询中奖记录成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryUserAwardRecord]运营端 查询中奖记录失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_credit_account")
    public BaseResponse<List<EsCreditAccountResponseDTO>> queryCreditAccount() {
        try {
            log.info("======================[ErpOperateController-queryCreditAccount]运营端 查询积分账户开始 ======================");
            List<EsCreditAccountVO> esCreditAccounts = repository.queryCreditAccountVOList();
            ArrayList<EsCreditAccountResponseDTO> list = new ArrayList<>();
            for (EsCreditAccountVO esCreditAccount : esCreditAccounts) {
                EsCreditAccountResponseDTO esCreditAccountResponseDTO = new EsCreditAccountResponseDTO();
                BeanUtils.copyProperties(esCreditAccount, esCreditAccountResponseDTO);
                list.add(esCreditAccountResponseDTO);
            }
            log.info("======================[ErpOperateController-queryCreditAccount]运营端 查询积分账户成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryCreditAccount]运营端 查询积分账户失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_credit_record")
    public BaseResponse<List<EsCreditRecordResponseDTO>> queryCreditRecord() {
        try {
            log.info("======================[ErpOperateController-queryCreditRecord]运营端 查询积分记录开始 ======================");
            List<EsCreditRecordVO> esCreditRecords = repository.queryCreditRecordVOList();
            ArrayList<EsCreditRecordResponseDTO> list = new ArrayList<>();
            for (EsCreditRecordVO esCreditRecord : esCreditRecords) {
                EsCreditRecordResponseDTO esCreditRecordResponseDTO = new EsCreditRecordResponseDTO();
                BeanUtils.copyProperties(esCreditRecord, esCreditRecordResponseDTO);
                list.add(esCreditRecordResponseDTO);
            }
            log.info("======================[ErpOperateController-queryCreditRecord]运营端 查询积分记录成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryCreditRecord]运营端 查询积分记录失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_activity_record")
    public BaseResponse<List<EsActivityRecordResponseDTO>> queryActivityRecord() {
        try {
            log.info("======================[ErpOperateController-queryActivityRecord]运营端 查询活动账户记录开始 ======================");
            List<EsActivityRecordVO> esActivityRecords = repository.queryEsActivityRecordVOList();
            ArrayList<EsActivityRecordResponseDTO> list = new ArrayList<>();
            for (EsActivityRecordVO esActivityRecord : esActivityRecords) {
                EsActivityRecordResponseDTO esActivityRecordResponseDTO = new EsActivityRecordResponseDTO();
                BeanUtils.copyProperties(esActivityRecord, esActivityRecordResponseDTO);
                list.add(esActivityRecordResponseDTO);
            }
            log.info("======================[ErpOperateController-queryActivityRecord]运营端 查询活动账户记录成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryActivityRecord]运营端 查询活动账户记录失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_user_behavior_rebate_order")
    public BaseResponse<List<EsUserBehaviorRebateOrderResponseDTO>> queryUserBehaviorRebateOrder() {
        try {
            log.info("======================[ErpOperateController-queryUserBehaviorRebateOrder]运营端 查询用户行为返利订单开始 ======================");
            List<EsUserBehaviorRebateOrderVO> esUserBehaviorRebateOrders = repository.queryEsUserBehaviorRebateOrderVOList();
            ArrayList<EsUserBehaviorRebateOrderResponseDTO> list = new ArrayList<>();
            for (EsUserBehaviorRebateOrderVO esUserBehaviorRebateOrder : esUserBehaviorRebateOrders) {
                EsUserBehaviorRebateOrderResponseDTO esUserBehaviorRebateOrderResponseDTO = new EsUserBehaviorRebateOrderResponseDTO();
                BeanUtils.copyProperties(esUserBehaviorRebateOrder, esUserBehaviorRebateOrderResponseDTO);
                list.add(esUserBehaviorRebateOrderResponseDTO);
            }
            log.info("======================[ErpOperateController-queryUserBehaviorRebateOrder]运营端 查询用户行为返利订单成功 ======================");
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
        }catch (Exception e){
            log.error("======================[ErpOperateController-queryUserBehaviorRebateOrder]运营端 查询用户行为返利订单失败 ======================", e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }
}

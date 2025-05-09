package com.lottery.trigger.http;

import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.OrderTradeTypeVO;
import com.lottery.domain.activity.service.ActivityPartakeService;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.activity.service.ActivitySkuProductService;
import com.lottery.domain.activity.service.armory.ActivityArmory;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.TradeEntity;
import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import com.lottery.domain.credit.service.CreditService;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.service.RebateService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.trigger.api.dto.req.ActivityDrawRequestDTO;
import com.lottery.trigger.api.dto.req.SkuProductShopCartRequestDTO;
import com.lottery.trigger.api.dto.req.UserActivityAccountRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityDrawResponseDTO;
import com.lottery.trigger.api.dto.res.SkuProductResponseDTO;
import com.lottery.trigger.api.dto.res.UserActivityAccountResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/activity")
public class LotteryActivityController implements LotteryActivityService {
    @Resource
    private StrategyArmory strategyArmory;
    @Resource
    private Lottery lottery;
    @Resource
    private ActivityArmory activityArmory;
    @Resource
    private UserAwardService userAwardService;
    @Resource
    private ActivityPartakeService activityPartakeService;
    @Resource
    private RebateService rebateService;
    @Resource
    private ActivityQuotaService activityQuotaService;
    @Resource
    private ActivitySkuProductService activitySkuProductService;
    @Resource
    private CreditService creditService;

    @Override
    @GetMapping("/armory")
    public BaseResponse<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("======================[LotteryActivityController-armory]整体装配开始 activityId:{} ======================", activityId);
            // 1. 活动装配 suk库存、对应次数列表、该活动信息 如果已存在缓存中，直接用就行
            activityArmory.assembleActivitySkuByActivityId(activityId);
            log.info("[LotteryActivityController-armory]活动装配成功 activityId:{}", activityId);
            // 2. 策略装配 该活动奖品列表、每个奖品数量、概率范围值、概率表、概率+权重表
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            log.info("[LotteryActivityController-armory]策略装配成功 activityId:{}", activityId);
            log.info("======================[LotteryActivityController-armory]整体装配成功 activityId:{} ======================", activityId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
        } catch (Exception e) {
            log.error("======================[LotteryActivityController-armory]整体装配失败 activityId:{} ======================", activityId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/draw")
    public BaseResponse<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("======================[LotteryActivityController-draw]用户抽奖开始 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || request.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            // 2. 创建抽奖单
            PartakeOrderResEntity partakeOrder = activityPartakeService.createPartakeOrder(request.getUserId(), request.getActivityId());
            log.info("[LotteryActivityController-draw]抽奖单 orderId:{}", partakeOrder.getOrderId());
            // 3. 执行抽奖
            log.info("[LotteryActivityController-draw]执行抽奖");
            LotteryResEntity lotteryResEntity = lottery.performLottery(LotteryReqEntity.builder().userId(partakeOrder.getUserId()).strategyId(partakeOrder.getStrategyId()).build());
            log.info("[LotteryActivityController-draw]抽奖结果 {}", lotteryResEntity);
            // 4. 写入中奖记录
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                    .userId(partakeOrder.getUserId())
                    .activityId(partakeOrder.getActivityId())
                    .strategyId(partakeOrder.getStrategyId())
                    .orderId(partakeOrder.getOrderId())
                    .awardConfig(lotteryResEntity.getAwardConfig())
                    .awardId(lotteryResEntity.getAwardId())
                    .awardTitle(lotteryResEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .build();
            userAwardService.saveUserAwardRecord(userAwardRecord);
            log.info("[LotteryActivityController-draw]写入中奖记录成功");
            // 5. 返回结果
            ActivityDrawResponseDTO result = ActivityDrawResponseDTO.builder()
                    .awardId(Math.toIntExact(lotteryResEntity.getAwardId()))
                    .awardTitle(lotteryResEntity.getAwardTitle())
                    .awardIndex(lotteryResEntity.getSort())
                    .build();
            log.info("======================[LotteryActivityController-draw]用户抽奖结束 userId:{} activityId:{} award:{} ======================", request.getUserId(), request.getActivityId(), result);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
        } catch (AppException e) {
            log.error("======================[LotteryActivityController-draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[LotteryActivityController-draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/calendar_sign_rebate")
    public BaseResponse<Boolean> calendarSignRebate(@RequestParam String userId) {
        try {
            log.info("======================[LotteryActivityController-calendarSignRebate]用户签到返现开始 userId:{} ======================", userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
            behaviorEntity.setOutBusinessNo(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            List<String> orderIds = rebateService.createRebateOrder(behaviorEntity);
            log.info("======================[LotteryActivityController-calendarSignRebate]用户签到返现成功 userId:{} orderIds:{} ======================", userId, orderIds);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
        } catch (AppException e) {
            log.error("======================[LotteryActivityController-calendarSignRebate]用户签到返现异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[LotteryActivityController-calendarSignRebate]用户签到返现异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/query_user_activity_account")
    public BaseResponse<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO requestDTO) {
        try {
            log.info("======================[LotteryActivityController-queryUserActivityAccount]查询用户抽奖次数信息开始 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId());
            // 1.参数校验
            if (StringUtils.isBlank(requestDTO.getUserId()) || requestDTO.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            // 2.查询
            ActivityAccountEntity activityAccountEntity = activityQuotaService.queryUserActivityAccount(requestDTO.getUserId(), requestDTO.getActivityId());
            // 3.返回结果
            UserActivityAccountResponseDTO res = new UserActivityAccountResponseDTO();
            BeanUtils.copyProperties(activityAccountEntity, res);
            log.info("======================[LotteryActivityController-queryUserActivityAccount]查询用户抽奖次数信息成功 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId());
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), res);
        } catch (AppException e) {
            log.error("======================[LotteryActivityController-queryUserActivityAccount]查询用户抽奖次数信息异常 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[LotteryActivityController-queryUserActivityAccount]查询用户抽奖次数信息异常 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/is_calendar_sign_rebate")
    public BaseResponse<Boolean> isCalendarSignRebate(@RequestParam String userId) {
        try {
            log.info("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到开始 userId:{} ======================", userId);
            String outBusinessNo = new SimpleDateFormat("yyyyMMdd").format(new Date());
            boolean b = rebateService.queryIsHaveRebateOrder(userId, outBusinessNo);
            log.info("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到成功 userId:{} 当日是否已签到:{} ======================", userId, b);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), b);
        } catch (AppException e) {
            log.error("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_sku_product_list_by_activity_id")
    public BaseResponse<List<SkuProductResponseDTO>> querySkuProductListByActivityId(@RequestParam Long activityId) {
        try {
            log.info("======================[querySkuProductListByActivityId]查询商品列表开始 activityId:{} ======================", activityId);
            if (activityId == null) {
                return new BaseResponse<>(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            List<SkuProductEntity> skuProductEntities = activitySkuProductService.querySkuProductEntityListByActivityId(activityId);
            ArrayList<SkuProductResponseDTO> skuProductResponseDTOS = new ArrayList<>();
            for (SkuProductEntity skuProductEntity : skuProductEntities) {
                SkuProductResponseDTO.ActivityCount activityCount = new SkuProductResponseDTO.ActivityCount();
                BeanUtils.copyProperties(skuProductEntity.getActivityCount(), activityCount);
                SkuProductResponseDTO skuProductResponseDTO = new SkuProductResponseDTO();
                BeanUtils.copyProperties(skuProductEntity, skuProductResponseDTO);
                skuProductResponseDTO.setActivityCount(activityCount);
                skuProductResponseDTOS.add(skuProductResponseDTO);
            }
            log.info("======================[querySkuProductListByActivityId]查询商品列表成功 activityId:{} ======================", activityId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), skuProductResponseDTOS);
        } catch (AppException e) {
            log.error("======================[querySkuProductListByActivityId]查询商品列表异常 activityId:{} ======================", activityId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[querySkuProductListByActivityId]查询商品列表异常 activityId:{} ======================", activityId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @GetMapping("/query_user_credit_account")
    public BaseResponse<BigDecimal> queryUserCreditAccount(@RequestParam String userId) {
        try {
            CreditAccountEntity creditAccountEntity = creditService.queryUserCreditAccount(userId);
            log.info("======================[queryUserCreditAccount]查询用户积分开始 userId:{} ======================", userId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), creditAccountEntity.getCreditAmount());
        } catch (AppException e) {
            log.error("======================[queryUserCreditAccount]查询用户积分异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[queryUserCreditAccount]查询用户积分异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }

    }

    @Override
    @PostMapping("/credit_pay_exchange_sku")
    public BaseResponse<Boolean> creditPayExchangeSku(@RequestBody SkuProductShopCartRequestDTO request) {
        try {
            log.info("======================[creditPayExchangeSku]积分兑换商品开始 userId:{} ======================", request.getUserId());
            // 1.创建增加抽奖次数的额度订单
            QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
            quotaOrderEntity.setUserId(request.getUserId());
            quotaOrderEntity.setSku(request.getSku());
            quotaOrderEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
            quotaOrderEntity.setOrderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade);
            UnpaidQuotaOrderEntity quotaOrder = activityQuotaService.createQuotaOrder(quotaOrderEntity);
            // 2.创建增加积分的积分订单
            TradeEntity tradeEntity = new TradeEntity();
            tradeEntity.setUserId(request.getUserId());
            tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
            tradeEntity.setTradeType(TradeTypeVO.REVERSE);
            tradeEntity.setOutBusinessNo(quotaOrder.getOutBusinessNo());
            tradeEntity.setAmount(quotaOrder.getPayAmount());
            String creditOrder = creditService.createCreditOrder(tradeEntity);
            log.info("======================[creditPayExchangeSku]积分兑换商品成功 userId:{} sku:{} orderId:{} ======================", request.getUserId(), request.getSku(), creditOrder);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
        } catch (AppException e) {
            log.error("======================[creditPayExchangeSku]积分兑换商品异常 userId:{} ======================", request.getUserId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[creditPayExchangeSku]积分兑换商品异常 userId:{} ======================", request.getUserId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

}

package com.lottery.trigger.http;

import com.lottery.domain.activity.model.entity.*;
import com.lottery.domain.activity.model.valobj.OrderTradeTypeVO;
import com.lottery.domain.activity.service.ActivityPartakeService;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.activity.service.ActivitySkuProductService;
import com.lottery.domain.activity.service.armory.ActivityArmory;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.event.SaveAwardRecordMessageEvent;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.TradeEntity;
import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import com.lottery.domain.credit.service.CreditService;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.service.RebateService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.Rule;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.adapter.repository.EsErpRepository;
import com.lottery.querys.model.valobj.UserAwardRecordVO;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.trigger.api.dto.req.*;
import com.lottery.trigger.api.dto.res.*;
import com.lottery.types.annotation.DCCValue;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixSync;
import com.lottery.types.annotation.PermissionCheck;
import com.lottery.types.annotation.RateLimiterAccessInterceptor;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.types.model.MyPage;
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
//@DubboService(version = "1.0")
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
    @DCCValue("degradeSwitch:close")
    private String degradeSwitch;
    @Resource
    private EsErpRepository esRepository;
    @Resource
    private ErpRepository repository;
    @Resource
    private Rule rule;
    @Resource
    private SaveAwardRecordMessageEvent saveAwardRecordMessageEvent;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    @PostMapping("/armory")
    @PermissionCheck(roles = {"ROLE_ADMIN"})
    @DeleteOldCacheWithPrefixSync(key = {Constants.RedisKey.ACTIVITY_SKU_LIST_KEY,Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY})
    public BaseResponse<Boolean> armory(@RequestParam Long activityId) {
        log.info("======================[LotteryActivityController-armory]预热开始 activityId:{} ======================", activityId);
        // 1. 活动装配 suk库存、对应次数列表、该活动信息
        activityArmory.assembleActivitySkuByActivityId(activityId);
        log.info("[LotteryActivityController-armory]预热活动信息 activityId:{}", activityId);
        // 2. 策略装配 该活动奖品列表、每个奖品数量、奖品信息、概率范围值、概率表、概率+权重表、规则树
        strategyArmory.assembleLotteryStrategyByActivityId(activityId);
        log.info("[LotteryActivityController-armory]预热策略信息 activityId:{}", activityId);
        // 3. 预热权重奖品strategy_rule_weight_200001
        rule.queryStrategyRuleWeight(activityId);
        log.info("[LotteryActivityController-armory]预热权重奖品信息 activityId:{}", activityId);
        log.info("======================[LotteryActivityController-armory]预热成功 activityId:{} ======================", activityId);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/draw")
    //每秒超过2次，频次限制 累计这种情况3次，黑名单拦截 24小时后解封
    @RateLimiterAccessInterceptor(key = "userId", fallbackMethod = "drawRateLimiterError", permitsPerSecond = 2, blacklistCount = 3)
    //超过1500ms无响应或出现异常，走drawHystrixError方法
//    @HystrixCommand(commandProperties = {
//            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
//    }, fallbackMethod = "drawHystrixError")
    public BaseResponse<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        log.info("======================[LotteryActivityController-draw]用户抽奖开始 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId());
        if ("open".equals(degradeSwitch)) {
            log.debug("======================[LotteryActivityController-draw]用户抽奖结束,已进行降级处理 ======================");
            return new BaseResponse<>(ResponseCode.DEGRADE_SWITCH.getCode(), ResponseCode.DEGRADE_SWITCH.getMessage());
        }
        // 1. 参数校验
        if (StringUtils.isBlank(request.getUserId()) || request.getActivityId() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        // 2. 创建抽奖单
        PartakeOrderResEntity partakeOrder = activityPartakeService.createPartakeOrder(request.getUserId(), request.getActivityId());
        log.debug("[LotteryActivityController-draw]抽奖单 orderId:{}", partakeOrder.getId());
        // 3. 执行抽奖
        log.debug("[LotteryActivityController-draw]执行抽奖");
        LotteryResEntity lotteryResEntity = lottery.doLottery(LotteryReqEntity.builder().userId(partakeOrder.getUserId()).strategyId(partakeOrder.getStrategyId()).activityId(partakeOrder.getActivityId()).build());
        log.debug("[LotteryActivityController-draw]抽奖结果 {}", lotteryResEntity);
        // 4. 异步写入中奖记录（通过MQ）
        UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                .userId(partakeOrder.getUserId())
                .activityId(partakeOrder.getActivityId())
                .strategyId(partakeOrder.getStrategyId())
                .userOrderId(partakeOrder.getId())
                .awardConfig(lotteryResEntity.getAwardConfig())
                .awardId(lotteryResEntity.getAwardId())
                .awardTitle(lotteryResEntity.getAwardTitle())
                .awardTime(lotteryResEntity.getAwardTime())
                .awardState(AwardStateVO.create)
                .build();
        try {
            SaveAwardRecordMessageEvent.SaveAwardRecordMessage msg = SaveAwardRecordMessageEvent.SaveAwardRecordMessage.builder()
                    .userId(userAwardRecord.getUserId())
                    .activityId(userAwardRecord.getActivityId())
                    .strategyId(userAwardRecord.getStrategyId())
                    .userOrderId(userAwardRecord.getUserOrderId())
                    .awardId(userAwardRecord.getAwardId())
                    .awardTitle(userAwardRecord.getAwardTitle())
                    .awardConfig(userAwardRecord.getAwardConfig())
                    .awardTime(userAwardRecord.getAwardTime())
                    .build();
            eventPublisher.publish(saveAwardRecordMessageEvent.topic(), saveAwardRecordMessageEvent.buildEventMessage(msg));
            log.debug("[LotteryActivityController-draw]异步写入中奖记录消息已发送");
        } catch (Exception e) {
            log.error("[LotteryActivityController-draw]MQ发送失败，降级为同步保存 userId:{}", request.getUserId(), e);
            userAwardService.saveUserAwardRecord(userAwardRecord);
        }
        // 5. 返回结果
        ActivityDrawResponseDTO result = ActivityDrawResponseDTO.builder()
                .awardId(lotteryResEntity.getAwardId())
                .awardTitle(lotteryResEntity.getAwardTitle())
                .awardIndex(lotteryResEntity.getSort())
                .build();
        log.info("======================[LotteryActivityController-draw]用户抽奖结束 userId:{} activityId:{} award:{} ======================", request.getUserId(), request.getActivityId(), result);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
    }

    public BaseResponse<ActivityDrawResponseDTO> drawRateLimiterError(ActivityDrawRequestDTO request) {
        log.error("!!!!!!!!!![LotteryActivityController-drawRateLimiterError]用户抽奖限流 userId:{} activityId:{} !!!!!!!!!!", request.getUserId(), request.getActivityId());
        return new BaseResponse<>(ResponseCode.RATE_LIMITER.getCode(), ResponseCode.RATE_LIMITER.getMessage());
    }

    public BaseResponse<ActivityDrawResponseDTO> drawHystrixError(ActivityDrawRequestDTO request, Throwable throwable) {
        String message = throwable.getMessage();
        if (message == null) {
            message = "响应超时,请稍后再试";
        }
        log.error("!!!!!!!!!![LotteryActivityController-drawHystrixError]用户抽奖熔断: {} userId:{} activityId:{} !!!!!!!!!!", message, request.getUserId(), request.getActivityId());
        return new BaseResponse<>(ResponseCode.HYSTRIX.getCode(), message);
    }

    @Override
    @PostMapping("/calendar_sign_rebate")
    public BaseResponse<Boolean> calendarSignRebate(@RequestBody CalendarSignRebateRequestDTO calendarSignRebateRequestDTO) {
        String userId = calendarSignRebateRequestDTO.getUserId();
        Long activityId = calendarSignRebateRequestDTO.getActivityId();
        log.info("======================[LotteryActivityController-calendarSignRebate]用户签到返现开始 userId:{} activityId:{} ======================", userId, activityId);
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId(userId);
        behaviorEntity.setActivityId(activityId);
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
        behaviorEntity.setOutBusinessNo(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        List<String> orderIds = rebateService.createRebateOrder(behaviorEntity);
        log.info("======================[LotteryActivityController-calendarSignRebate]用户签到返现成功 userId:{} rebateOrderIds:{} ======================", userId, orderIds);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/query_user_activity_account")
    public BaseResponse<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO requestDTO) {
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
    }

    @Override
    @PostMapping("/is_calendar_sign_rebate")
    public BaseResponse<Boolean> isCalendarSignRebate(@RequestBody CalendarSignRebateRequestDTO calendarSignRebateRequestDTO) {
        String userId = calendarSignRebateRequestDTO.getUserId();
        Long activityId = calendarSignRebateRequestDTO.getActivityId();
        log.info("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到开始 userId:{} activityId:{}======================", userId, activityId);
        String outBusinessNo = new SimpleDateFormat("yyyyMMdd").format(new Date());
        boolean b = rebateService.queryIsHaveRebateOrder(userId, activityId, outBusinessNo);
        log.info("======================[LotteryActivityController-isCalendarSignRebate]查询用户当日是否已签到成功 userId:{} activityId:{} 当日是否已签到:{} ======================", userId, activityId, b);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), b);
    }

    @Override
    @GetMapping("/query_sku_product_list_by_activity_id")
    public BaseResponse<List<SkuProductResponseDTO>> querySkuProductListByActivityId(@RequestParam Long activityId) {
        log.info("======================[LotteryActivityController-querySkuProductListByActivityId]查询积分兑换商品sku列表开始 activityId:{} ======================", activityId);
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
        log.info("======================[LotteryActivityController-querySkuProductListByActivityId]查询积分兑换sku商品列表成功 activityId:{} ======================", activityId);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), skuProductResponseDTOS);
    }

    @Override
    @PostMapping("/query_user_credit_account")
    public BaseResponse<BigDecimal> queryUserCreditAccount(@RequestBody UserCreditAccountRequestDTO requestDTO) {
        String userId = requestDTO.getUserId();
        Long activityId = requestDTO.getActivityId();
        log.info("======================[LotteryActivityController-queryUserCreditAccount]查询用户积分开始 userId:{} activityId:{} ======================", userId, activityId);
        // 1.参数校验
        if (StringUtils.isBlank(userId) || activityId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        CreditAccountEntity creditAccountEntity = creditService.queryUserCreditAccount(userId, activityId);
        log.info("======================[LotteryActivityController-queryUserCreditAccount]查询用户积分成功 userId:{} activityId:{} creditAmount:{} ======================", userId, activityId, creditAccountEntity.getCreditAmount());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), creditAccountEntity.getCreditAmount());
    }

    @Override
    @PostMapping("/credit_pay_exchange_sku")
    public BaseResponse<Boolean> creditPayExchangeSku(@RequestBody SkuProductShopCartRequestDTO request) {
        log.info("======================[LotteryActivityController-creditPayExchangeSku]积分兑换商品开始 userId:{} activityId:{} skuId:{} ======================", request.getUserId(), request.getActivityId(), request.getSku());
        // 1.创建增加抽奖次数的额度订单
        QuotaOrderEntity quotaOrderEntity = new QuotaOrderEntity();
        quotaOrderEntity.setUserId(request.getUserId());
        quotaOrderEntity.setActivityId(request.getActivityId());
        quotaOrderEntity.setSku(request.getSku());
        quotaOrderEntity.setOutBusinessNo(RandomStringUtils.randomNumeric(12));
        quotaOrderEntity.setOrderTradeTypeVO(OrderTradeTypeVO.credit_pay_trade);
        UnpaidQuotaOrderEntity quotaOrder = activityQuotaService.createQuotaOrder(quotaOrderEntity);
        log.info("[LotteryActivityController-creditPayExchangeSku]创建增加抽奖次数的额度订单成功 userId:{} activityId:{} skuId:{} orderId:{}", request.getUserId(), request.getActivityId(), request.getSku(), quotaOrder.getOrderId());
        // 2.创建扣减积分的积分订单
        TradeEntity tradeEntity = new TradeEntity();
        tradeEntity.setUserId(request.getUserId());
        tradeEntity.setActivityId(request.getActivityId());
        tradeEntity.setTradeName(TradeNameVO.CONVERT_SKU);
        tradeEntity.setTradeType(TradeTypeVO.REVERSE);
        tradeEntity.setOutBusinessNo(quotaOrder.getOutBusinessNo());
        tradeEntity.setAmount(quotaOrder.getPayAmount());
        String creditOrder = creditService.createCreditOrder(tradeEntity);
        log.info("[LotteryActivityController-creditPayExchangeSku]创建扣减积分的积分订单成功 userId:{} activityId:{} sku:{} creditOrder:{}", request.getUserId(), request.getActivityId(), request.getSku(), creditOrder);
        log.info("======================[LotteryActivityController-creditPayExchangeSku]积分兑换商品成功 userId:{} activityId:{} sku:{} orderId:{} ======================", request.getUserId(), request.getActivityId(), request.getSku(), creditOrder);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

//    @Override
//    @GetMapping("/query_user_award_record_by_activity_id")
//    public BaseResponse<List<EsUserAwardRecordResponseDTO>> queryUserAwardRecordByActivityId(@RequestParam Long activityId) {
//        log.info("======================[LotteryActivityController-queryUserAwardRecordByActivityId]查询中奖播报开始 activityId:{} ======================", activityId);
//        List<EsUserAwardRecordVO> esUserAwardRecords = esRepository.queryEsUserAwardRecordVOList(activityId);
//        ArrayList<EsUserAwardRecordResponseDTO> list = new ArrayList<>();
//        for (EsUserAwardRecordVO esUserAwardRecord : esUserAwardRecords) {
//            EsUserAwardRecordResponseDTO esUserAwardRecordResponseDTO = new EsUserAwardRecordResponseDTO();
//            BeanUtils.copyProperties(esUserAwardRecord, esUserAwardRecordResponseDTO);
//            list.add(esUserAwardRecordResponseDTO);
//        }
//        log.info("======================[LotteryActivityController-queryUserAwardRecordByActivityId]查询中奖播报成功 activityId:{} ======================", activityId);
//        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
//    }

    @GetMapping("/query_my_award_record")
    @Override
    public BaseResponse<MyPage<UserAwardRecordResponseDTO>> queryMyAwardRecordByPage(@RequestParam(defaultValue = "1") Integer pageNum,
                                                                                     @RequestParam(defaultValue = "36") Integer pageSize,
                                                                                     @RequestParam Long activityId,
                                                                                     @RequestParam String userId) {
        log.info("======================[LotteryActivityController-queryUserAwardRecordByActivityId]查询个人中奖记录开始 activityId:{} userId:{}======================", activityId, userId);
        MyPage<UserAwardRecordVO> userAwardRecords = repository.queryUserAwardRecordVOListByPage(pageNum, pageSize, activityId, userId);
        ArrayList<UserAwardRecordResponseDTO> list = new ArrayList<>();
        if (userAwardRecords.getItems() == null) {
            log.info("======================[LotteryActivityController-queryUserAwardRecordByActivityId]查询个人中奖记录成功 activityId:{} userId:{}======================", activityId, userId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), new MyPage<>());
        }
        for (UserAwardRecordVO userAwardRecord : userAwardRecords.getItems()) {
            UserAwardRecordResponseDTO userAwardRecordResponseDTO = new UserAwardRecordResponseDTO();
            BeanUtils.copyProperties(userAwardRecord, userAwardRecordResponseDTO);
            list.add(userAwardRecordResponseDTO);
        }
        MyPage<UserAwardRecordResponseDTO> myPage = new MyPage<>();
        myPage.setItems(list);
        myPage.setTotal(userAwardRecords.getTotal());
        log.info("======================[LotteryActivityController-queryUserAwardRecordByActivityId]查询个人中奖记录成功 activityId:{} userId:{}======================", activityId, userId);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), myPage);
    }

    @Override
    @PostMapping("/add_lottery_quota")
    public BaseResponse<Boolean> addLotteryQuota(@RequestBody AddLotteryQuotaRequestDTO request) {
        log.info("======================[LotteryActivityController-addLotteryQuota]赠送抽奖额度开始 userId:{} activityId:{} behaviorRebateId:{} ======================", request.getUserId(), request.getActivityId(),request.getBehaviorRebateId());
        String userId = request.getUserId();
        Long activityId = request.getActivityId();
        Long behaviorRebateId = request.getBehaviorRebateId();
        BehaviorEntity behaviorEntity = new BehaviorEntity();
        behaviorEntity.setUserId(userId);
        behaviorEntity.setActivityId(activityId);
        behaviorEntity.setBehaviorRebateId(behaviorRebateId);
        behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.ACTIVITY_GIFT);
        behaviorEntity.setOutBusinessNo(new SimpleDateFormat("yyyy").format(new Date()));
        rebateService.createRebateOrderOfGift(behaviorEntity);
        log.info("======================[LotteryActivityController-addLotteryQuota]赠送抽奖额度成功 userId:{} activityId:{} rebateOrderId:{} ======================", userId,activityId,behaviorRebateId);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
    }

    @Override
    @PostMapping("/is_add_lottery_quota")
    public BaseResponse<Boolean> isAddLotteryQuota(@RequestBody AddLotteryQuotaRequestDTO request) {
        String userId = request.getUserId();
        Long activityId = request.getActivityId();
        Long behaviorRebateId = request.getBehaviorRebateId();
        log.info("======================[LotteryActivityController-isAddLotteryQuota]查询用户是否已领取抽奖额度开始 userId:{} activityId:{} behaviorRebateId:{} ======================", userId, activityId,behaviorRebateId);
        boolean b = rebateService.isAddLotteryQuota(userId, activityId,behaviorRebateId);
        log.info("======================[LotteryActivityController-isAddLotteryQuota]查询用户是否已领取抽奖额度成功 userId:{} activityId{} behaviorRebateId:{} 是否领取:{} ======================", userId, activityId,behaviorRebateId, b);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), b);
    }

}

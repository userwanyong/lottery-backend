package com.lottery.trigger.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lottery.domain.activity.model.entity.DeliveryOrderEntity;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.credit.event.CreditAdjustSuccessMessageEvent;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.event.BaseEvent;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 积分调整成功消息（轻量版：由 SendMessageTaskJob 本地分发调用，去 MQ）
 */
@Slf4j
@Component
public class CreditAdjustSuccessCustomer {
    public static final String TOPIC = "credit_adjust_success";

    @Resource
    private ActivityQuotaService activityQuotaService;

    public void listener(String message) {
        try {
            log.info("[CreditAdjustSuccessCustomer]监听到积分账户调整成功消息，进行交易商品发放 topic: {} message: {}", TOPIC, message);
            BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> eventMessage = JSON.parseObject(message, new TypeReference<BaseEvent.EventMessage<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>>() {
            }.getType());
            CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage creditAdjustSuccessMessage = eventMessage.getData();
            // 积分发货
            DeliveryOrderEntity deliveryOrderEntity = new DeliveryOrderEntity();
            deliveryOrderEntity.setUserId(creditAdjustSuccessMessage.getUserId());
            deliveryOrderEntity.setActivityId(creditAdjustSuccessMessage.getActivityId());
            deliveryOrderEntity.setOutBusinessNo(creditAdjustSuccessMessage.getOutBusinessNo());
            activityQuotaService.updateQuotaOrder(deliveryOrderEntity);
            log.info("[CreditAdjustSuccessCustomer]积分账户调整成功消息，进行交易商品发货成功 topic: {} message: {}", TOPIC, message);
        } catch (AppException ae) {
            if (ResponseCode.INDEX_DUP.getCode() == ae.getCode()) {
                log.warn("[CreditAdjustSuccessCustomer]积分账户调整成功消息，进行交易商品发货，消费重复 topic: {} message: {}", TOPIC, message, ae);
                return;
            }
            throw ae;
        } catch (Exception e) {
            log.error("[CreditAdjustSuccessCustomer]积分账户调整成功消息，进行交易商品发货失败 topic: {} message: {}", TOPIC, message, e);
            throw e;
        }
    }

}

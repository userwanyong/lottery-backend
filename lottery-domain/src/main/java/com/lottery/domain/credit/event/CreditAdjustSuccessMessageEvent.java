package com.lottery.domain.credit.event;

import com.lottery.types.event.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 永
 * 调整积分账户更新成功消息（轻量版：topic 硬编码，去 rabbitmq 配置）
 */
@Component
public class CreditAdjustSuccessMessageEvent extends BaseEvent<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage> {

    @Override
    public EventMessage<CreditAdjustSuccessMessage> buildEventMessage(CreditAdjustSuccessMessage data) {
        return EventMessage.<CreditAdjustSuccessMessageEvent.CreditAdjustSuccessMessage>builder()
                .id(RandomStringUtils.randomNumeric(11))
                .timestamp(new Date())
                .data(data)
                .build();
    }

    @Override
    public String topic() {
        return "credit_adjust_success";
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreditAdjustSuccessMessage {
        /** 用户ID */
        private String userId;
        /** 活动ID */
        private Long activityId;
        /** 订单ID */
        private String orderId;
        /** 交易金额 */
        private BigDecimal amount;
        /** 业务仿重ID - 外部透传。返利、行为等唯一标识 */
        private String outBusinessNo;
    }

}

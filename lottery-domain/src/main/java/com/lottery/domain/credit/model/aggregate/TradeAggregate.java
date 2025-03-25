package com.lottery.domain.credit.model.aggregate;

import com.lottery.domain.credit.model.entity.CreditAccountEntity;
import com.lottery.domain.credit.model.entity.CreditOrderEntity;
import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;

/**
 * @author 永
 * 交易聚合对象
 */
@Data
public class TradeAggregate {
    // 用户ID
    private String userId;
    // 积分账户实体
    private CreditAccountEntity creditAccountEntity;
    // 积分订单实体
    private CreditOrderEntity creditOrderEntity;

    public static CreditAccountEntity buildCreditAccountEntity(String userId, BigDecimal creditAmount) {
        return CreditAccountEntity.builder().userId(userId).creditAmount(creditAmount).build();
    }

    public static CreditOrderEntity buildCreditOrderEntity(String userId, TradeNameVO tradeName, TradeTypeVO tradeType, BigDecimal tradeAmount, String outBusinessNo) {
        return CreditOrderEntity.builder()
                .userId(userId)
                .orderId(RandomStringUtils.randomNumeric(12))
                .tradeName(tradeName)
                .tradeType(tradeType)
                .tradeAmount(tradeAmount)
                .outBusinessNo(outBusinessNo)
                .build();
    }

}

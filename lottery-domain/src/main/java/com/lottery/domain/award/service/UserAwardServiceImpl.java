package com.lottery.domain.award.service;

import com.lottery.domain.award.event.SendAwardMessageEvent;
import com.lottery.domain.award.model.aggregate.UserAwardRecordAggregate;
import com.lottery.domain.award.model.entity.DistributeAwardEntity;
import com.lottery.domain.award.model.entity.TaskEntity;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.TaskStateVO;
import com.lottery.domain.award.repository.UserAwardRepository;
import com.lottery.domain.award.service.distribute.DistributeAward;
import com.lottery.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 永
 * 奖品领域实现类
 */
@Service
@Slf4j
public class UserAwardServiceImpl implements UserAwardService{
    private final UserAwardRepository userAwardRepository;
    private final SendAwardMessageEvent sendAwardMessageEvent;
    private final Map<String, DistributeAward> distributeAwardMap;

    public UserAwardServiceImpl(UserAwardRepository userAwardRepository, SendAwardMessageEvent sendAwardMessageEvent, Map<String, DistributeAward> distributeAwardMap) {
        this.userAwardRepository = userAwardRepository;
        this.sendAwardMessageEvent = sendAwardMessageEvent;
        this.distributeAwardMap = distributeAwardMap;
    }

    @Override
    public void saveUserAwardRecord(UserAwardRecordEntity userAwardRecordEntity) {
        //构建消息对象
        SendAwardMessageEvent.SendAwardMessage message = new SendAwardMessageEvent.SendAwardMessage();
        message.setAwardId(userAwardRecordEntity.getAwardId());
        message.setAwardTitle(userAwardRecordEntity.getAwardTitle());
        message.setUserId(userAwardRecordEntity.getUserId());
        message.setOrderId(userAwardRecordEntity.getOrderId());
        message.setAwardConfig(userAwardRecordEntity.getAwardConfig());
        BaseEvent.EventMessage<SendAwardMessageEvent.SendAwardMessage> sendAwardMessageEventMessage = sendAwardMessageEvent.buildEventMessage(message);
        //构建任务对象
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setUserId(userAwardRecordEntity.getUserId());
        taskEntity.setTopic(sendAwardMessageEvent.topic());
        taskEntity.setMessageId(sendAwardMessageEventMessage.getId());
        taskEntity.setMessage(sendAwardMessageEventMessage);
        taskEntity.setState(TaskStateVO.create);
        //构建聚合对象
        UserAwardRecordAggregate userAwardRecordAggregate = new UserAwardRecordAggregate();
        userAwardRecordAggregate.setTaskEntity(taskEntity);
        userAwardRecordAggregate.setUserAwardRecordEntity(userAwardRecordEntity);
        //存储聚合对象
        userAwardRepository.saveUserAwardRecord(userAwardRecordAggregate);
    }

    @Override
    public void distributeAward(DistributeAwardEntity distributeAwardEntity) {
        // 查询awardKey
        String awardKey = userAwardRepository.queryAwardKey(distributeAwardEntity.getAwardId());
        // 找对应发奖服务
        DistributeAward distributeAward = distributeAwardMap.get(awardKey);
        if (distributeAward==null) {
            log.error("分发奖品，对应的服务不存在 awardKey:{}", awardKey);
            return;
//            throw new RuntimeException("分发奖品，奖品" + awardKey + "对应的服务不存在");
        }
        // 调用服务发奖
        distributeAward.giveOutPrizes(distributeAwardEntity);

    }
}

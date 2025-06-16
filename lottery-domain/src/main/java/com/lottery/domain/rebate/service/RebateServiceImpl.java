package com.lottery.domain.rebate.service;

import com.lottery.domain.rebate.event.SendRebateMessageEvent;
import com.lottery.domain.rebate.model.aggregate.RebateAggregate;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.entity.TaskEntity;
import com.lottery.domain.rebate.model.valobj.RebateVO;
import com.lottery.domain.rebate.model.valobj.TaskStateVO;
import com.lottery.domain.rebate.repository.RebateRepository;
import com.lottery.types.common.Constants;
import com.lottery.types.event.BaseEvent;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 * 返利领域实现类
 */
@Service
public class RebateServiceImpl implements RebateService {
    @Resource
    private RebateRepository rebateRepository;
    @Resource
    private SendRebateMessageEvent sendRebateMessageEvent;

    @Override
    public List<String> createRebateOrder(BehaviorEntity behaviorEntity) {
        // 1.根据行为类型+activityId查询返利配置表
        List<RebateVO> rebateVOList = rebateRepository.queryRebateConfig(behaviorEntity);
        // 2.构建聚合对象 一个行为可能对应多个返利配置
        List<String> rebateOrders = new ArrayList<>();
        List<RebateAggregate> aggregates = new ArrayList<>();
        for (RebateVO rebateVO : rebateVOList) {
            // 业务id 用户ID_活动ID_返利类型_返利配置_外部透彻业务ID
            String bizId = behaviorEntity.getUserId()+Constants.UNDERLINE+behaviorEntity.getActivityId()+ Constants.UNDERLINE+ rebateVO.getRebateType()+ Constants.UNDERLINE+rebateVO.getRebateConfig() + Constants.UNDERLINE + behaviorEntity.getOutBusinessNo();
            // 构建返利单
            RebateOrderEntity rebateOrderEntity = new RebateOrderEntity();
            BeanUtils.copyProperties(rebateVO, rebateOrderEntity);
            rebateOrderEntity.setOutBusinessNo(behaviorEntity.getOutBusinessNo());
            rebateOrderEntity.setActivityId(behaviorEntity.getActivityId());
            rebateOrderEntity.setBizId(bizId);
            rebateOrderEntity.setUserId(behaviorEntity.getUserId());
            rebateOrderEntity.setOrderId(RandomStringUtils.randomNumeric(12));

            rebateOrders.add(rebateOrderEntity.getOrderId());

            // 构建mq消息对象
            SendRebateMessageEvent.RebateMessage message = SendRebateMessageEvent.RebateMessage.builder()
                    .userId(behaviorEntity.getUserId())
                    .activityId(behaviorEntity.getActivityId())
                    .rebateType(rebateVO.getRebateType())
                    .rebateConfig(rebateVO.getRebateConfig())
                    .rebateDesc(rebateVO.getRebateDesc())
                    .bizId(bizId)
                    .build();
            BaseEvent.EventMessage<SendRebateMessageEvent.RebateMessage> rebateMessageEventMessage = sendRebateMessageEvent.buildEventMessage(message);
            // 构建任务对象
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setUserId(behaviorEntity.getUserId());
            taskEntity.setActivityId(behaviorEntity.getActivityId());
            taskEntity.setTopic(sendRebateMessageEvent.topic());
            taskEntity.setMessageId(rebateMessageEventMessage.getId());
            taskEntity.setMessage(rebateMessageEventMessage);
            taskEntity.setState(TaskStateVO.create);

            // 构建聚合
            RebateAggregate rebateAggregate = new RebateAggregate();
            rebateAggregate.setUserId(behaviorEntity.getUserId());
            rebateAggregate.setActivityId(behaviorEntity.getActivityId());
            rebateAggregate.setRebateOrderEntity(rebateOrderEntity);
            rebateAggregate.setTaskEntity(taskEntity);
            aggregates.add(rebateAggregate);
        }
        // 3.保存聚合对象
        rebateRepository.saveRebateAggregate(aggregates);
        // 4.返回返利单id集合
        return rebateOrders;
    }

    @Override
    public List<RebateOrderEntity> queryRebateOrder(String userId, String outBusinessNo) {
        return rebateRepository.queryRebateOrder(userId,outBusinessNo);
    }

    @Override
    public boolean queryIsHaveRebateOrder(String userId, Long activityId,String outBusinessNo) {
        return rebateRepository.queryIsHaveRebateOrder(userId,activityId,outBusinessNo);
    }

}

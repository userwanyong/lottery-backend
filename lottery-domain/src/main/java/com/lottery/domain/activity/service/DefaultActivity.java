package com.lottery.domain.activity.service;

import com.lottery.domain.activity.repository.ActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @author 永
 * 抽奖活动服务
 */
@Service
public class DefaultActivity extends AbstractActivity{
    public DefaultActivity(ActivityRepository activityRepository) {
        super(activityRepository);
    }
}

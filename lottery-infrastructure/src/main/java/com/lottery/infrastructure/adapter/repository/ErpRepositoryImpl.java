package com.lottery.infrastructure.adapter.repository;


import com.lottery.infrastructure.dao.ActivityMapper;
import com.lottery.infrastructure.dao.po.Activity;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.ActivityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 */
@Repository
public class ErpRepositoryImpl implements ErpRepository {
    @Resource
    private ActivityMapper activityMapper;

    @Override
    public List<ActivityVO> queryActivityVOList() {
        List<Activity> activities = activityMapper.selectList(null);
        ArrayList<ActivityVO> list = new ArrayList<>();
        for (Activity activity : activities) {
            ActivityVO activityVO = new ActivityVO();
            BeanUtils.copyProperties(activity, activityVO);
            list.add(activityVO);
        }
        return list;
    }

    @Override
    public void addActivityVO(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activityMapper.insert(activity);
    }

    @Override
    public void updateActivityVO(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activityMapper.updateById(activity);
    }

    @Override
    public void deleteActivityVO(Long activityId) {
        activityMapper.deleteById(activityId);
    }
}

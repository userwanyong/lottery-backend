package com.lottery.infrastructure.adapter.repository;


import com.lottery.infrastructure.dao.ActivityCountMapper;
import com.lottery.infrastructure.dao.ActivityMapper;
import com.lottery.infrastructure.dao.ActivitySkuMapper;
import com.lottery.infrastructure.dao.po.Activity;
import com.lottery.infrastructure.dao.po.ActivityCount;
import com.lottery.infrastructure.dao.po.ActivitySku;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.ActivityCountVO;
import com.lottery.querys.model.valobj.ActivitySkuVO;
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
    @Resource
    private ActivityCountMapper activityCountMapper;
    @Resource
    private ActivitySkuMapper activitySkuMapper;

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

    @Override
    public List<ActivityCountVO> queryActivityCountVOList() {
        List<ActivityCount> activityCounts = activityCountMapper.selectList(null);
        ArrayList<ActivityCountVO> list = new ArrayList<>();
        for (ActivityCount activityCount : activityCounts) {
            ActivityCountVO activityCountVO = new ActivityCountVO();
            BeanUtils.copyProperties(activityCount, activityCountVO);
            list.add(activityCountVO);
        }
        return list;
    }

    @Override
    public void addActivityCountVO(ActivityCountVO activityCountVO) {
        ActivityCount activityCount = new ActivityCount();
        BeanUtils.copyProperties(activityCountVO, activityCount);
        activityCountMapper.insert(activityCount);
    }

    @Override
    public void updateActivityCountVO(ActivityCountVO activityCountVO) {
        ActivityCount activityCount = new ActivityCount();
        BeanUtils.copyProperties(activityCountVO, activityCount);
        activityCountMapper.updateById(activityCount);
    }

    @Override
    public void deleteActivityCountVO(Long activityCountId) {
        activityCountMapper.deleteById(activityCountId);
    }

    @Override
    public List<ActivitySkuVO> queryActivitySkuVOList() {
        List<ActivitySku> activitySkus = activitySkuMapper.selectList(null);
        ArrayList<ActivitySkuVO> list = new ArrayList<>();
        for (ActivitySku activitySku : activitySkus) {
            ActivitySkuVO activitySkuVO = new ActivitySkuVO();
            BeanUtils.copyProperties(activitySku, activitySkuVO);
            list.add(activitySkuVO);
        }
        return list;
    }

    @Override
    public void addActivitySkuVO(ActivitySkuVO activitySkuVO) {
        ActivitySku activitySku = new ActivitySku();
        BeanUtils.copyProperties(activitySkuVO, activitySku);
        activitySkuMapper.insert(activitySku);
    }

    @Override
    public void updateActivitySkuVO(ActivitySkuVO activitySkuVO) {
        ActivitySku activitySku = new ActivitySku();
        BeanUtils.copyProperties(activitySkuVO, activitySku);
        activitySkuMapper.updateById(activitySku);
    }

    @Override
    public void deleteActivitySkuVO(Long activitySkuId) {
        activitySkuMapper.deleteById(activitySkuId);
    }
}

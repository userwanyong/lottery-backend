package com.lottery.querys.adapter.repository;


import com.lottery.querys.model.valobj.ActivityCountVO;
import com.lottery.querys.model.valobj.ActivityVO;

import java.util.List;

/**
 * @author 永
 */
public interface ErpRepository {
    List<ActivityVO> queryActivityVOList();

    void addActivityVO(ActivityVO activityVO);

    void updateActivityVO(ActivityVO activityVO);

    void deleteActivityVO(Long activityId);

    List<ActivityCountVO> queryActivityCountVOList();

    void addActivityCountVO(ActivityCountVO activityCountVO);

    void updateActivityCountVO(ActivityCountVO activityCountVO);

    void deleteActivityCountVO(Long activityCountId);
}

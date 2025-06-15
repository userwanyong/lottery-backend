package com.lottery.querys.adapter.repository;


import com.lottery.querys.model.valobj.*;

import java.util.List;

/**
 * @author 永
 */
public interface EsErpRepository {
    List<EsUserOrderVO> queryEsUserOrderVOList();

    List<EsActivityAccountVO> queryEsActivityAccountVOList();

    List<EsUserAwardRecordVO> queryEsUserAwardRecordVOList();

    List<EsCreditAccountVO> queryCreditAccountVOList();

    List<EsCreditRecordVO> queryCreditRecordVOList();

    List<EsActivityRecordVO> queryEsActivityRecordVOList();

    List<EsUserBehaviorRebateOrderVO> queryEsUserBehaviorRebateOrderVOList();

}

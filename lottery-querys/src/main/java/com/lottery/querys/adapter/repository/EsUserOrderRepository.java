package com.lottery.querys.adapter.repository;


import com.lottery.querys.model.valobj.EsUserOrderVO;

import java.util.List;

/**
 * @author 永
 */
public interface EsUserOrderRepository {
    List<EsUserOrderVO> queryEsUserOrderVOList();
}

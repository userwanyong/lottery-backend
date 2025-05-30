package com.lottery.infrastructure.es;

import com.lottery.infrastructure.es.po.EsUserOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 永
 * @description es查询
 */
@Mapper
public interface EsUserOrderMapper {
    List<EsUserOrder> queryUserOrderListEs();
}





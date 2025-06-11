package com.lottery.infrastructure.adapter.repository;


import com.lottery.infrastructure.es.EsActivityAccountMapper;
import com.lottery.infrastructure.es.EsUserOrderMapper;
import com.lottery.infrastructure.es.po.EsActivityAccount;
import com.lottery.infrastructure.es.po.EsActivityOrder;
import com.lottery.infrastructure.es.po.EsUserOrder;
import com.lottery.querys.adapter.repository.EsUserOrderRepository;
import com.lottery.querys.model.valobj.EsActivityAccountVO;
import com.lottery.querys.model.valobj.EsUserOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 */
@Repository
public class EsUserOrderRepositoryImpl implements EsUserOrderRepository {

    @Resource
    private EsUserOrderMapper esUserOrderMapper;
    @Resource
    private EsActivityAccountMapper esActivityAccountMapper;

    @Override
    public List<EsUserOrderVO> queryEsUserOrderVOList() {
        List<EsUserOrder> esUserOrders = esUserOrderMapper.queryUserOrderListEs();
        ArrayList<EsUserOrderVO> list = new ArrayList<>();
        for (EsUserOrder esUserOrder : esUserOrders) {
            EsUserOrderVO esUserOrderVO = new EsUserOrderVO();
            BeanUtils.copyProperties(esUserOrder, esUserOrderVO);
            list.add(esUserOrderVO);
        }
        return list;
    }

    @Override
    public List<EsActivityAccountVO> queryEsActivityAccountVOList() {
        List<EsActivityAccount> esActivityAccounts = esActivityAccountMapper.queryActivityAccountListEs();
        ArrayList<EsActivityAccountVO> list = new ArrayList<>();
        for (EsActivityAccount esActivityAccount : esActivityAccounts) {
            EsActivityAccountVO esActivityAccountVO = new EsActivityAccountVO();
            BeanUtils.copyProperties(esActivityAccount, esActivityAccountVO);
            list.add(esActivityAccountVO);
        }
        return list;
    }
}

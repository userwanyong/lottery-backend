package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.CreditAccount;
import com.lottery.infrastructure.es.po.EsCreditAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 永
 * @description 针对表【credit_account(积分账户表)】的数据库操作Mapper
 */
@Mapper
public interface EsCreditAccountMapper extends BaseMapper<CreditAccount> {

    List<EsCreditAccount> queryCreditAccount();
}





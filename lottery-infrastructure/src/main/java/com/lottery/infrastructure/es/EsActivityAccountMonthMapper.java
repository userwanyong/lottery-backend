package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.ActivityAccountMonth;
import com.lottery.infrastructure.es.po.EsActivityAccountMonth;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【activity_account_month(抽奖活动账户表-月次数)】的数据库操作Mapper
 */
@Mapper
public interface EsActivityAccountMonthMapper extends BaseMapper<EsActivityAccountMonth> {

}





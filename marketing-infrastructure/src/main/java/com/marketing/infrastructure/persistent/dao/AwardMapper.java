package com.marketing.infrastructure.persistent.dao;

import com.marketing.infrastructure.persistent.po.Award;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 永
* @description 针对表【award(奖品表)】的数据库操作Mapper
*/
@Mapper
public interface AwardMapper extends BaseMapper<Award> {

    List<Award> queryAwardList();
}





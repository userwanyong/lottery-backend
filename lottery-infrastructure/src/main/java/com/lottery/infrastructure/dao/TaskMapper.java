package com.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
* @author 永
* @description 针对表【task(任务表，消息补偿)】的数据库操作Mapper
*/
@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    @Select("select * from task " +
            "where (state = 'fail' and update_time >= date_sub(now(), interval 600 second)) " +
            "or (state = 'create' and update_time <= date_sub(now(), interval 60 second)) " +
            "limit 10")
    List<Task> queryNoSendMessageTaskList();

    @Update("update task set state = 'completed', update_time = now() where user_id = #{userId} and message_id = #{messageId}")
    void updateTaskSendMessageCompleted(Task taskReq);

    @Update("update task set state = 'fail', update_time = now() where user_id = #{userId} and message_id = #{messageId}")
    void updateTaskSendMessageFail(Task taskReq);
}

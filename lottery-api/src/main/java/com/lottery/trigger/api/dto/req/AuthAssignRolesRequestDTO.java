package com.lottery.trigger.api.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 全量覆盖用户角色：传入最终目标角色 ID 列表，空列表=清空角色
 */
@Data
public class AuthAssignRolesRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Long> roleIds;
}

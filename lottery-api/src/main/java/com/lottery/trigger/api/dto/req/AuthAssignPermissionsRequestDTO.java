package com.lottery.trigger.api.dto.req;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 全量覆盖角色权限：传入最终目标权限 ID 列表，空列表=清空权限
 */
@Data
public class AuthAssignPermissionsRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<Long> permissionIds;
}

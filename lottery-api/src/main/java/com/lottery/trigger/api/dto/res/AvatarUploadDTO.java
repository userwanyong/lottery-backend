package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AvatarUploadDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String url;
}

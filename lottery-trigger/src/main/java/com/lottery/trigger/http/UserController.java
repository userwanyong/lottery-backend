package com.lottery.trigger.http;

import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.service.impl.IUserService;
import com.lottery.trigger.api.dto.req.RefreshTokenRequestDTO;
import com.lottery.trigger.api.dto.req.UserLoginRequestDTO;
import com.lottery.trigger.api.dto.res.UserLoginResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public BaseResponse<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO request) {
        log.info("[UserController-login] login request received, username={}", request.getUsername());

        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        UserVO userVO = userService.login(userDTO);

        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setAccessToken(userVO.getToken());
        responseDTO.setRefreshToken(userVO.getRefreshToken());
        responseDTO.setExpiresIn(userVO.getExpiresIn());

        log.info("[UserController-login] login succeeded, userId={}", responseDTO.getId());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    @PostMapping("/refresh")
    public BaseResponse<UserLoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("[UserController-refresh] refresh token request received");

        UserVO userVO = userService.refreshToken(request.getRefreshToken());

        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setAccessToken(userVO.getToken());
        responseDTO.setRefreshToken(userVO.getRefreshToken());
        responseDTO.setExpiresIn(userVO.getExpiresIn());

        log.info("[UserController-refresh] refresh token succeeded, userId={}", responseDTO.getId());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        UserUtils userUtils = ThreadUtils.getUser();
        Long userId = userUtils == null ? null : userUtils.getId();

        log.info("[UserController-logout] logout request received, userId={}", userId);
        if (userId != null) {
            userService.logout(userId);
        }
        log.info("[UserController-logout] logout succeeded, userId={}", userId);

        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}

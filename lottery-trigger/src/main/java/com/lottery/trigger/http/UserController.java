package com.lottery.trigger.http;

import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.service.impl.IUserService;
import com.lottery.trigger.api.dto.req.LoginByCodeRequestDTO;
import com.lottery.trigger.api.dto.req.LogoutRequestDTO;
import com.lottery.trigger.api.dto.req.PasswordLoginRequestDTO;
import com.lottery.trigger.api.dto.req.RefreshTokenRequestDTO;
import com.lottery.trigger.api.dto.req.SendLoginCodeRequestDTO;
import com.lottery.trigger.api.dto.res.UserInfoResponseDTO;
import com.lottery.trigger.api.dto.res.UserLoginResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 认证授权入口：全部委托 auth-service（Dubbo Triple RPC）完成，
 * 登录方式与管理端开闭状态实时同步（/user/login-methods）。
 */
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

    private static final String OAUTH_LOGIN_FRAGMENT = "oauth";
    private static final String OAUTH_ERROR_FRAGMENT = "oauth_error";

    @Resource
    private IUserService userService;

    @Value("${auth.frontend-base-url:http://localhost:8000}")
    private String frontendBaseUrl;

    /**
     * 当前租户启用的登录方式，登录页据此动态渲染，与 auth-service 管理端开闭同步
     */
    @GetMapping("/login-methods")
    public BaseResponse<List<String>> listLoginMethods() {
        List<String> methods = userService.listEnabledLoginMethods();
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), methods);
    }

    /**
     * 账号密码登录（用户名或邮箱）
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginResponseDTO> loginByPassword(@Valid @RequestBody PasswordLoginRequestDTO request) {
        log.info("[UserController-loginByPassword] request received, username={}", request.getUsername());

        UserVO userVO = userService.loginByPassword(request.getUsername(), request.getPassword());
        UserLoginResponseDTO responseDTO = buildLoginResponse(userVO);

        log.info("[UserController-loginByPassword] login succeeded, userId={}", responseDTO.getId());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    /**
     * 发送登录验证码（邮箱/短信，具体通道由登录方式编码决定）
     */
    @PostMapping("/send-code")
    public BaseResponse<Void> sendLoginCode(@Valid @RequestBody SendLoginCodeRequestDTO request) {
        log.info("[UserController-sendLoginCode] request received, method={}", request.getMethod());

        userService.sendLoginCode(request.getMethod(), request.getTarget());

        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    /**
     * 验证码登录：未知邮箱/手机号由 auth-service 自动注册
     */
    @PostMapping("/login-by-code")
    public BaseResponse<UserLoginResponseDTO> loginByCode(@Valid @RequestBody LoginByCodeRequestDTO request) {
        log.info("[UserController-loginByCode] request received, method={}", request.getMethod());

        UserVO userVO = userService.loginByCode(request.getMethod(), request.getTarget(), request.getCode());
        UserLoginResponseDTO responseDTO = buildLoginResponse(userVO);

        log.info("[UserController-loginByCode] login succeeded, userId={}", responseDTO.getId());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    /**
     * OAuth 登录入口：302 跳转到第三方授权页
     */
    @GetMapping("/oauth/{provider}/authorize")
    public void oauthAuthorize(@PathVariable String provider, HttpServletResponse response) throws IOException {
        log.info("[UserController-oauthAuthorize] request received, provider={}", provider);
        String authorizeUrl = userService.buildOAuthAuthorizeUrl(provider);
        log.info("[UserController-oauthAuthorize] redirecting to provider authorize page, provider={}", provider);
        response.sendRedirect(authorizeUrl);
    }

    /**
     * OAuth 提供方回调：完成登录后 302 回前端登录页，令牌通过 URL fragment 携带（不会发往服务器）
     */
    @GetMapping("/oauth/{provider}/callback")
    public void oauthCallback(@PathVariable String provider, String code, String state,
                              HttpServletResponse response) throws IOException {
        log.info("[UserController-oauthCallback] request received, provider={}", provider);
        try {
            UserVO userVO = userService.handleOAuthCallback(provider, code, state);
            String fragment = OAUTH_LOGIN_FRAGMENT + "="
                    + encode(userVO.getAccessToken()) + "|"
                    + encode(userVO.getRefreshToken()) + "|"
                    + userVO.getExpiresIn();
            log.info("[UserController-oauthCallback] oauth login succeeded, provider={}, userId={}", provider, userVO.getId());
            redirectToFrontend(response, fragment);
        } catch (Exception e) {
            log.warn("[UserController-oauthCallback] oauth login failed, provider={}, reason={}", provider, e.getMessage());
            redirectToFrontend(response, OAUTH_ERROR_FRAGMENT + "=" + encode(e.getMessage()));
        }
    }

    /**
     * 刷新令牌对（轮换，旧的 refreshToken 立即失效）
     */
    @PostMapping("/refresh")
    public BaseResponse<UserLoginResponseDTO> refresh(@Valid @RequestBody RefreshTokenRequestDTO request) {
        log.info("[UserController-refresh] refresh token request received");

        UserVO userVO = userService.refreshToken(request.getRefreshToken());
        UserLoginResponseDTO responseDTO = buildLoginResponse(userVO);

        log.info("[UserController-refresh] refresh token succeeded, userId={}", responseDTO.getId());
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    /**
     * 当前登录用户信息（需携带 accessToken）
     */
    @GetMapping("/me")
    public BaseResponse<UserInfoResponseDTO> me() {
        UserUtils userUtils = ThreadUtils.getUser();
        UserVO userVO = userService.getUserById(userUtils.getId());

        UserInfoResponseDTO responseDTO = new UserInfoResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setNickname(userVO.getNickname());
        responseDTO.setAvatar(userVO.getAvatar());
        responseDTO.setRoles(userVO.getRoles());
        responseDTO.setPermissions(userVO.getPermissions());

        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    /**
     * 登出：拉黑 accessToken 并撤销 refreshToken
     */
    @PostMapping("/logout")
    public BaseResponse<Void> logout(@RequestBody(required = false) LogoutRequestDTO request, HttpServletRequest httpRequest) {
        UserUtils userUtils = ThreadUtils.getUser();
        Long userId = userUtils == null ? null : userUtils.getId();
        String refreshToken = request == null ? null : request.getRefreshToken();

        log.info("[UserController-logout] logout request received, userId={}", userId);
        if (userId != null) {
            userService.logout(extractAccessToken(httpRequest), refreshToken);
        }
        log.info("[UserController-logout] logout succeeded, userId={}", userId);

        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    private UserLoginResponseDTO buildLoginResponse(UserVO userVO) {
        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setNickname(userVO.getNickname());
        responseDTO.setAvatar(userVO.getAvatar());
        responseDTO.setRoles(userVO.getRoles());
        responseDTO.setPermissions(userVO.getPermissions());
        responseDTO.setAccessToken(userVO.getAccessToken());
        responseDTO.setRefreshToken(userVO.getRefreshToken());
        responseDTO.setExpiresIn(userVO.getExpiresIn());
        return responseDTO;
    }

    private void redirectToFrontend(HttpServletResponse response, String fragment) throws IOException {
        String target = UriComponentsBuilder.fromHttpUrl(frontendBaseUrl)
                .path("/user/login")
                .fragment(fragment)
                .build()
                .toUriString();
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader(HttpHeaders.LOCATION, target);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }
        return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
    }

    private String encode(String value) {
        return value == null ? "" : URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

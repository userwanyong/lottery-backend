package com.lottery.trigger.http;

import com.lottery.domain.auth.model.vo.AuthUserUpdateVO;
import com.lottery.domain.auth.model.vo.AuthUserVO;
import com.lottery.domain.auth.model.vo.OAuthBindingVO;
import com.lottery.domain.user.model.vo.OAuthCallbackResultVO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.service.impl.IUserService;
import com.lottery.trigger.api.dto.req.AuthUserUpdateRequestDTO;
import com.lottery.trigger.api.dto.req.LoginByCodeRequestDTO;
import com.lottery.trigger.api.dto.req.LogoutRequestDTO;
import com.lottery.trigger.api.dto.req.PasswordLoginRequestDTO;
import com.lottery.trigger.api.dto.req.ProfileBindContactRequestDTO;
import com.lottery.trigger.api.dto.req.ProfilePasswordRequestDTO;
import com.lottery.trigger.api.dto.req.RefreshTokenRequestDTO;
import com.lottery.trigger.api.dto.req.SendLoginCodeRequestDTO;
import com.lottery.trigger.api.dto.res.AuthOAuthBindingDTO;
import com.lottery.trigger.api.dto.res.AuthUserDTO;
import com.lottery.trigger.api.dto.res.AvatarUploadDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

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
     * OAuth 提供方回调：登录流完成后 302 回前端登录页（令牌经 URL fragment 携带，不会发往服务器）；
     * 绑定流（个人中心发起）完成后 302 回个人中心页。
     */
    @GetMapping("/oauth/{provider}/callback")
    public void oauthCallback(@PathVariable String provider, String code, String state,
                              HttpServletResponse response) throws IOException {
        log.info("[UserController-oauthCallback] request received, provider={}", provider);
        try {
            OAuthCallbackResultVO resultVO = userService.handleOAuthCallback(provider, code, state);
            if (resultVO.isLogin()) {
                UserVO userVO = resultVO.getUser();
                String fragment = OAUTH_LOGIN_FRAGMENT + "="
                        + encode(userVO.getAccessToken()) + "|"
                        + encode(userVO.getRefreshToken()) + "|"
                        + userVO.getExpiresIn();
                log.info("[UserController-oauthCallback] oauth login succeeded, provider={}, userId={}", provider, userVO.getId());
                redirectToFrontend(response, "/user/login", fragment);
            } else if (resultVO.isBindSuccess()) {
                log.info("[UserController-oauthCallback] oauth bind succeeded, provider={}", provider);
                redirectToFrontend(response, "/account/center", "bind=success");
            } else {
                log.warn("[UserController-oauthCallback] oauth flow failed, provider={}, message={}", provider, resultVO.getMessage());
                redirectToFrontend(response, "/user/login",
                        OAUTH_ERROR_FRAGMENT + "=" + encode(resultVO.getMessage()));
            }
        } catch (Exception e) {
            log.warn("[UserController-oauthCallback] oauth login failed, provider={}, reason={}", provider, e.getMessage());
            redirectToFrontend(response, "/user/login", OAUTH_ERROR_FRAGMENT + "=" + encode(e.getMessage()));
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

    // ==================== 个人中心 ====================

    /**
     * 个人完整资料（含角色/权限/联系方式验证状态）
     */
    @GetMapping("/profile")
    public BaseResponse<AuthUserDTO> getProfile() {
        UserUtils userUtils = requireCurrentUser();
        return success(toAuthUserDTO(userService.getProfile(userUtils.getId())));
    }

    /**
     * 更新自己的资料（昵称/头像/邮箱/手机/真实姓名/性别/生日）
     */
    @PutMapping("/profile")
    public BaseResponse<Void> updateProfile(@RequestBody AuthUserUpdateRequestDTO request) {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-updateProfile] request received, userId={}", userUtils.getId());
        userService.updateProfile(userUtils.getId(), toAuthUserUpdateVO(request));
        return success();
    }

    /**
     * 上传头像（≤2MB，jpg/png/gif/webp），返回 URL；前端随后通过 updateProfile 持久化
     */
    @PostMapping("/profile/avatar")
    public BaseResponse<AvatarUploadDTO> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        UserUtils userUtils = requireCurrentUser();
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("头像文件不能为空");
        }
        log.info("[UserController-uploadAvatar] request received, userId={}, filename={}, size={}",
                userUtils.getId(), file.getOriginalFilename(), file.getSize());
        String url = userService.uploadAvatar(userUtils.getId(),
                file.getOriginalFilename(), file.getContentType(), file.getBytes());

        // 上传成功后直接落库到个人资料
        AuthUserUpdateVO updateVO = new AuthUserUpdateVO();
        updateVO.setAvatar(url);
        userService.updateProfile(userUtils.getId(), updateVO);

        AvatarUploadDTO dto = new AvatarUploadDTO();
        dto.setUrl(url);
        return success(dto);
    }

    /**
     * 修改自己的密码
     */
    @PutMapping("/profile/password")
    public BaseResponse<Void> changePassword(@Valid @RequestBody ProfilePasswordRequestDTO request) {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-changePassword] request received, userId={}", userUtils.getId());
        userService.changePassword(userUtils.getId(), request.getOldPassword(), request.getNewPassword());
        return success();
    }

    /**
     * 已绑定的第三方账号
     */
    @GetMapping("/profile/oauth-bindings")
    public BaseResponse<List<AuthOAuthBindingDTO>> listOAuthBindings() {
        UserUtils userUtils = requireCurrentUser();
        List<AuthOAuthBindingDTO> items = userService.listOAuthBindings(userUtils.getId()).stream()
                .map(binding -> {
                    AuthOAuthBindingDTO dto = new AuthOAuthBindingDTO();
                    dto.setId(binding.getId());
                    dto.setProvider(binding.getProvider());
                    dto.setProviderUid(binding.getProviderUid());
                    dto.setCreatedAt(binding.getCreatedAt());
                    return dto;
                }).collect(Collectors.toList());
        return success(items);
    }

    /**
     * 发起第三方账号绑定授权（302 到提供方）
     */
    @GetMapping("/profile/oauth-bindings/{provider}/authorize")
    public void oauthBindAuthorize(@PathVariable String provider, HttpServletResponse response) throws IOException {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-oauthBindAuthorize] request received, provider={}", provider);
        String authorizeUrl = userService.buildBindAuthorizeUrl(userUtils.getId(), provider);
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 解绑第三方账号
     */
    @DeleteMapping("/profile/oauth-bindings/{provider}")
    public BaseResponse<Void> unbindOAuth(@PathVariable String provider) {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-unbindOAuth] request received, userId={}, provider={}", userUtils.getId(), provider);
        userService.unbindOAuth(userUtils.getId(), provider);
        return success();
    }

    /**
     * 绑定邮箱（验证码先经 /user/send-code 发送）
     */
    @PostMapping("/profile/email")
    public BaseResponse<Void> bindEmail(@Valid @RequestBody ProfileBindContactRequestDTO request) {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-bindEmail] request received, userId={}", userUtils.getId());
        userService.bindEmail(userUtils.getId(), request.getMethod(), request.getTarget(), request.getCode());
        return success();
    }

    /**
     * 解绑邮箱
     */
    @DeleteMapping("/profile/email")
    public BaseResponse<Void> unbindEmail() {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-unbindEmail] request received, userId={}", userUtils.getId());
        userService.unbindEmail(userUtils.getId());
        return success();
    }

    /**
     * 绑定手机号（验证码先经 /user/send-code 发送）
     */
    @PostMapping("/profile/phone")
    public BaseResponse<Void> bindPhone(@Valid @RequestBody ProfileBindContactRequestDTO request) {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-bindPhone] request received, userId={}", userUtils.getId());
        userService.bindPhone(userUtils.getId(), request.getMethod(), request.getTarget(), request.getCode());
        return success();
    }

    /**
     * 解绑手机号
     */
    @DeleteMapping("/profile/phone")
    public BaseResponse<Void> unbindPhone() {
        UserUtils userUtils = requireCurrentUser();
        log.info("[UserController-unbindPhone] request received, userId={}", userUtils.getId());
        userService.unbindPhone(userUtils.getId());
        return success();
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

    private void redirectToFrontend(HttpServletResponse response, String path, String fragment) throws IOException {
        String target = UriComponentsBuilder.fromHttpUrl(frontendBaseUrl)
                .path(path)
                .fragment(fragment)
                .build()
                .toUriString();
        response.setStatus(HttpStatus.FOUND.value());
        response.setHeader(HttpHeaders.LOCATION, target);
    }

    private UserUtils requireCurrentUser() {
        UserUtils userUtils = ThreadUtils.getUser();
        if (userUtils == null || userUtils.getId() == null) {
            throw new IllegalArgumentException("未获取到当前登录用户");
        }
        return userUtils;
    }

    private AuthUserUpdateVO toAuthUserUpdateVO(AuthUserUpdateRequestDTO request) {
        AuthUserUpdateVO vo = new AuthUserUpdateVO();
        vo.setUsername(request.getUsername());
        vo.setPassword(request.getPassword());
        vo.setEmail(request.getEmail());
        vo.setPhone(request.getPhone());
        vo.setNickname(request.getNickname());
        vo.setAvatar(request.getAvatar());
        vo.setStatus(request.getStatus());
        vo.setRealName(request.getRealName());
        vo.setGender(request.getGender());
        vo.setBirthday(request.getBirthday());
        return vo;
    }

    private AuthUserDTO toAuthUserDTO(AuthUserVO vo) {
        AuthUserDTO dto = new AuthUserDTO();
        dto.setId(vo.getId());
        dto.setUsername(vo.getUsername());
        dto.setEmail(vo.getEmail());
        dto.setPhone(vo.getPhone());
        dto.setNickname(vo.getNickname());
        dto.setAvatar(vo.getAvatar());
        dto.setStatus(vo.getStatus());
        dto.setRoles(vo.getRoles());
        dto.setPermissions(vo.getPermissions());
        dto.setRealName(vo.getRealName());
        dto.setGender(vo.getGender());
        dto.setBirthday(vo.getBirthday());
        dto.setEmailVerified(vo.getEmailVerified());
        dto.setPhoneVerified(vo.getPhoneVerified());
        dto.setCreatedAt(vo.getCreatedAt());
        dto.setLastLoginAt(vo.getLastLoginAt());
        return dto;
    }

    private <T> BaseResponse<T> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    private <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), data);
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

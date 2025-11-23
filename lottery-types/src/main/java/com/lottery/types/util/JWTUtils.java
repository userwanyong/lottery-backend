package com.lottery.types.util;


import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.jwt.JWT;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 永
 */
@Slf4j
public class JWTUtils {
    static final String key = "wanyj.xybjz";

    /**
     * 生成JWT
     *
     * @param id   用户id
     * @param name 用户名
     * @return JWT
     */
    public static String createJWT(Long id, String name,Integer role) {
        return JWT.create()
                .setPayload("uid", id)
                .setPayload("uname", name)
                .setPayload("urole",role)
                .setExpiresAt(DateTime.now().offsetNew(DateField.MONTH, 1))
                .setKey(key.getBytes())
                .sign();
    }

    public static boolean verify(String token) {
        try {
            return JWT.of(token).setKey(key.getBytes()).verify();
        } catch (Exception e) {
            return false;
        }
    }

    public static UserUtils getUser(String token) {
        try {
            JWT jwt = JWT.of(token).setKey(key.getBytes());
            UserUtils userUtils = new UserUtils();
            userUtils.setId(NumberUtil.parseLong(jwt.getPayload("uid").toString()));
            userUtils.setUsername((String) jwt.getPayload("uname"));
            userUtils.setRole(NumberUtil.parseInt(jwt.getPayload("urole").toString()));
            return userUtils;
        } catch (Exception e) {
            log.warn("获取用户登录状态信息失败");
            return null;
        }
    }
}


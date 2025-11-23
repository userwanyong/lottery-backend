package com.lottery.types.util;



/**
 * @author 永
 */
public class ThreadUtils {

    static ThreadLocal<UserUtils> userThreadLocal = new ThreadLocal<>();

    public static UserUtils getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(UserUtils userUtils) {
        userThreadLocal.set(userUtils);
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }
}

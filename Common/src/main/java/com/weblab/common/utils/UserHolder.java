package com.weblab.common.utils;

// 处理请求，保存当前登录用户的 uid
public class UserHolder {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal();

    private UserHolder() {
    }

    // 保存请求用户id
    public static void saveLoginUser(String loginUser){
        threadLocal.set(loginUser);
    }

    public static String getLoginUser() {
        if (threadLocal.get() == null) {
            throw new IllegalStateException("用户没有登录！");
        }
        return threadLocal.get();
    }

}
package com.ye.wm.common;

/**
 * 基于ThreadLocal封装工具类，保存和获取当前用户信息
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal =new ThreadLocal<Long>();

    public static Long getCurrentId() {
        return threadLocal.get();

    }

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }
}

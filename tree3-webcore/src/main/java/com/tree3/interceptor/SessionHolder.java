package com.tree3.interceptor;

import com.tree3.pojo.dto.UserDTO;

/**
 * 将用户的信息 保存至 请求所在的线程中去
 */
public class SessionHolder {
    private static final ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void saveUser(UserDTO user) {
        threadLocal.set(user);
    }

    public static UserDTO getUser() {
        return threadLocal.get();
    }

    public static void removeUser() {
        threadLocal.remove();
    }
}
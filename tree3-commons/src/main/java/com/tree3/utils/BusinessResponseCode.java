package com.tree3.utils;

public enum BusinessResponseCode {
    SUCCESS(2000, "成功"),
    PARAM_HANDLE_ERROR(1000, "参数处理异常"),
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    USER_NOT_LOGGED_IN(2001, "用户未登录，请登录！"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误！"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用！"),
    USER_NOT_EXIST(2004, "用户不存在！"),
    USER_ALREADY_EXISTED(2005, "用户已存在！"),
    PERMISSION_DENIED(2006, "权限不足！"),

    NOT_FOUND(4004, "未找到对应资源"),

    BUSINESS_ERROR(5000, "业务逻辑异常！"),
    UNKNOWN_EXCEPTION(-5000, "不确定的异常类型!"),
    AUTH_EXCEPTION(5001, "认证异常"),
    ASSERT_EXCEPTION(5002, "Assert断言，抛出异常"),
    MEANINGLESS_USE(5100, "无意义的操作");


    private Integer code;
    private String message;

    BusinessResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}

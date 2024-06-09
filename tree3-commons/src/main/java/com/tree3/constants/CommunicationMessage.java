package com.tree3.constants;

/**
 * <p>
 * 交互信息
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 16:48 </p>
 */
public interface CommunicationMessage {
    String Login_SUCCESS = "登录成功！！";
    String Login_Fail = "登录失败！！";
    String ILLEGAL_PARAMETER = "录入的信息不合法";
    String UNKNOWN_USER = "未知的用户，请检查输入/请先注册！！";
    String IncorrectPassword = "密码不正确";
    String ILLEGAL_TOKEN = "无效/非法的token";
    String UNSUPPORTED_CONTENT_TYPES = "不支持的消息内容类型";
//    String Login_Fail = "登录失败！！";
//    String Login_Fail = "登录失败！！";
}

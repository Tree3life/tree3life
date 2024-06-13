package com.tree3.pojo.command;

/**
 * <p>
 * 命令类型：
 * 会话类命令 0-10
 * 私聊类命令 10-99
 * 群聊类命令 100-199
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 10:27 </p>
 */
public enum Command {

    //region会话相关 0-10
    /**
     * 会话相关
     */
    SessionCorrelation(0),
    Connect(1),
    DisConnect(-1),
    Login(2),
    Ping(3),
    Pong(4),
    /**
     * 退出
     */
    Logout(-2),
    /**
     * 用户已下线，用于实现 群聊消息的 未读消息功能
     */
    Offline(-3),
    //endregion会话相关

    //region 私聊相关 10-99
    /**
     * 私聊相关
     */
    PrivateChatCorrelation(10),
    /**
     * 文本 私聊
     */
    PrivateChatText(11),
    /**
     * 表情包 私聊
     */
    PrivateChatExpressionPackage(12),
    /**
     * 文件 私聊
     */
    PrivateChatFile(13),
    /**
     * 查询与好友的聊天记录
     */
    PrivateQueryHistory(21),
    //endregion 私聊相关


    //region 群聊相关 100-199
    /**
     * 群聊相关
     */
    GroupChatCorrelation(100),
    /**
     * 文本 群聊，文件群聊后期会被转换为网址
     */
    GroupChatText(101),
    /**
     * 表情包 群聊
     */
    GroupChatExpressionPackage(102),
    /**
     * 文件 群聊
     */
    GroupChatFile(103),
    /**
     * 建群
     */
    CreatChatGroup(104),
    /**
     * 编辑群信息
     */
    EditChatGroup(105),
    /**
     * 解散群
     */
    DeleteChatGroup(106),
    /**
     * 加入群
     */
    JoinChatGroup(107),
    /**
     * 添加群成员
     */
    AddChatGroupMembers(108),
    /**
     * 删除群成员
     */
    RemoveChatGroupMembers(109),
    /**
     * 退群
     */
    QuitChatGroup(110),


    /**
     * 查询群聊的聊天记录
     */
    GroupChatQueryHistory(199),
    //endregion 群聊相关

    //region 响应相关 200-299
    /**
     * 响应相关
     */
    ResponseCorrelation(200),
    /**
     * 登录操作的响应
     */
    ResponseLogin(201),
    /**
     * 查询好友聊天记录的响应
     */
    ResponsePrivateQueryHistory(202),
    ResponseGroupChatQueryHistory(203),
    //endregion 响应相关


    //region 异常相关
    /**
     * 不需要区分处理的一般异常
     */
    ExceptionCorrelation(300),
    //endregion 异常相关

    //未知的命令
    ERROR(-2);
    private final Integer type;

    public Integer getType() {
        return type;
    }

    //Enum中的构造方法: 从抽象层面上来理解，它是对enum中成员变量构造函数的抽取
    Command(Integer type) {
        this.type = type;
    }

    /**
     * 粗略匹配 命令类型，获取该命令的类型
     * <p>
     * ！此方法可以使用更高效的算法进行优化
     *
     * @param commandType
     * @return SessionCorrelation|PrivateChatCorrelation|GroupChatCorrelation
     */
    public static Command roughlyMatch(Integer commandType) {
        int high = commandType / 10;
        if (high == 0) {
            return SessionCorrelation;
        } else if (1 <= high && high < 10) {
            return PrivateChatCorrelation;
        } else if (10 <= high && high < 20) {
            return GroupChatCorrelation;
        }
        return ERROR;
    }

    /**
     * 精确匹配操作类型
     *
     * @param commandType
     * @return
     */
    public static Command match(Integer commandType) {
        for (Command item : Command.values()) {
            if (item.type.equals(commandType)) {
                return item;
            }
        }
        return ERROR;
    }
}

/**
 *本文件用于存储和聊天业务的控制相关的所有常量信息
 *！！！要和后端保持一致
 */
//region 消息状态
export const messageStatus = {
    //未送达
    Undelivered: -1,
    //已送达&未读
    Delivered: 0,
    //已读
    Unread: 1,
    //已读
    Read: 2,
    //撤回
    Revocation: 3,
};

/**
 * 命令类型
 * @type {{"0", SessionCorrelation()}}
 */
export const commandType = {
    //region会话相关 0-10
    SessionCorrelation: 0,//会话相关
    Connect: 1,
    DisConnect: -1,
    Login: 2,
    Logout: -2,
    Ping: 3,
    Pong: 4,
    //endregion会话相关
    //region 私聊相关 10-99
    PrivateChatCorrelation: 10,//私聊相关
    PrivateChatText: 11,//文本 私聊
    PrivateChatExpressionPackage: 12,//表情包 私聊
    PrivateChatFile: 13,//文件 私聊

    PrivateQueryHistory: 21,//查询与好友的聊天记录

//endregion 私聊相关
    //region 群聊相关 100-199
    GroupChatCorrelation: 100,//群聊相关
    GroupChatText: 101,//文本 群聊
    GroupChatExpressionPackage: 102,//表情包 群聊
    GroupChatFile: 103,//文件 群聊
    CreatChatGroup: 104,//建群
    EditChatGroup: 105,//编辑群信息
    DeleteChatGroup: 106,//解散群
    JoinChatGroup: 107,//加入群
    AddChatGroupMembers: 108,//添加群成员
    RemoveChatGroupMembers: 109,//删除群成员
    QuitChatGroup: 110,//退群
    //endregion 群聊相关

    //region 响应相关 200-299
    ResponseCorrelation: 200,//响应相关
    ResponseLogin: 201,//登录操作的响应
    ResponsePrivateQueryHistory: 202,//查询好友聊天记录的响应

//endregion 响应相关

//region 异常相关
    ExceptionCorrelation: 300,//不需要区分处理的一般异常

//endregion 异常相关
    //未知的命令
    ERROR: -2,
}

/**
 * 消息类型
 消息类型的设计原则： 会话类消息 0-10，私聊类消息 10-99，群聊类消息 100-199
 */
export const messageType = {
    //region 消息类型
    //消息头：消息的控制信息
    MessageHead: -1,
    LoginRequestMessage: 0,
    LoginResponseMessage: 1,
    PingMessage: 2,
    PongMessage: 3,

    PrivateChatRequestMessage: 11,
    PrivateChatResponseMessage: 12,

    GroupCreateRequestMessage: 101,
    GroupCreateResponseMessage: 102,
    GroupJoinRequestMessage: 103,
    GroupJoinResponseMessage: 104,
    GroupQuitRequestMessage: 105,
    GroupQuitResponseMessage: 106,
    GroupChatRequestMessage: 107,
    GroupChatResponseMessage: 108,
    GroupMembersRequestMessage: 109,
    GroupMembersResponseMessage: 110
}
//endregion 消息状态

//region body类型 即消息体的数据类型
export const contentType = {
    ByteStream: 0,
    Text: 1,
    File: 2,
    Blob: 3,
}
//endregion body类型 即消息体的数据类型

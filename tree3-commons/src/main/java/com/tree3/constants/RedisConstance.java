package com.tree3.constants;

public interface RedisConstance {
    /**
     * 用户token的存活时间---30min
     */
    long LOGIN_USER_TTL = 30;
    String APPLICATION_NAME = "TREE3LIFE:";
    String ONLINE_KEY = APPLICATION_NAME + "Online:";//在线用户
    String TOKEN_KEY = APPLICATION_NAME + "TOKEN_SESSION:";//代表用户认证tokenkey
    String TIMEOUT_KEY = APPLICATION_NAME + "TIMEOUT:";//代表 超时字符串前缀
    String PHONE_KEY = APPLICATION_NAME + "PHONE:";//代表 超时字符串前缀

    String USER_LIKE_PREFIX = APPLICATION_NAME + "USER_LIKE_";  //用户喜欢视频列表前缀
    String USER_DISLIKE_PREFIX = APPLICATION_NAME + "USER_DISLIKE_";//用户不喜欢的列表
    String VIDEO_LIKE_COUNT_PREFIX = APPLICATION_NAME + "VIDEO_LIKE_COUNT_";//用户喜欢视频数量前缀
    String VIDEO_PLAYED_COUNT_PREFIX = APPLICATION_NAME + "VIDEO_PLAYED_COUNT_";//视频播放次数前缀
    String CHAT_HISTORY = APPLICATION_NAME + "CHAT_HISTORY";
    String RabbitMQ_MSGID = APPLICATION_NAME + "RabbitMQ_MSGID";
}


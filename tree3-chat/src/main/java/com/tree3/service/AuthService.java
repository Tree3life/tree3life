package com.tree3.service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 9:52 </p>
 */
public interface AuthService {

    /**
     * 登录
     * 1. login(token)，从redis中获取用户的消息
     * 2. 将userid和channel进行绑定
     *
     * @return 登录成功返回 true, 否则返回 false
     */
    boolean login(ChannelHandlerContext context, TextWebSocketFrame frame);

    /**
     * @param username
     * @param password
     * @return
     */
    boolean logout(ChannelHandlerContext username, TextWebSocketFrame password);

    /**
     * @param context
     * @param frame
     */
    void handlePing(ChannelHandlerContext context, TextWebSocketFrame frame);

    /**
     * 将心跳的职责交割客户端维护，即 由客户端定时Ping，服务器不主动发送Ping消息，
     * 由此 从以上职责的划分来讲，服务端是不需要处理Pong消息的
     *
     * @param context
     * @param frame
     */
    void handlePong(ChannelHandlerContext context, TextWebSocketFrame frame);
}

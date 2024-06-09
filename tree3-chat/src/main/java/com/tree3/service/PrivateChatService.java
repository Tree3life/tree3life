package com.tree3.service;

import com.tree3.pojo.entity.ChatHistory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;

/**
 * <p>
 * 私聊
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 9:55 </p>
 */
public interface PrivateChatService {


    /**
     * 查询与好友的聊天记录
     *
     * @return
     */
    List<ChatHistory> queryChatHistory(ChannelHandlerContext context, TextWebSocketFrame frame);

    void sendMessage();

    /**
     * 私聊消息的转发
     *
     * @param context
     * @param frame
     */
    void transferChatMessage(ChannelHandlerContext context, TextWebSocketFrame frame);
}

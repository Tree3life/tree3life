package com.tree3.utils;

import com.tree3.pojo.command.Command;
import com.tree3.pojo.message.MessageResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/20 10:56 </p>
 */
public class ResponseHelperWebSocket {

    /**
     * 发送一条失败的系统消息
     *
     * @param message
     * @return
     */
    public static TextWebSocketFrame fail(String message) {
        MessageResponse messageText = new MessageResponse();
        messageText.setCommandType(Command.ExceptionCorrelation.getType());
        messageText.setTo(-1);//-1 代表系统
        messageText.setCreateTime(new Date());
        messageText.setData(message);

        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(messageText)
        );
    }

    public static TextWebSocketFrame fail(String message,Command exceptionType) {
        MessageResponse messageText = new MessageResponse();
        messageText.setCommandType(exceptionType.getType());
        messageText.setTo(-1);//-1 代表系统
        messageText.setCreateTime(new Date());
        messageText.setData(message);

        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(messageText)
        );
    }

    /**
     * 发送一条成功的系统消息
     *
     * @param message
     * @return
     */
    public static TextWebSocketFrame success(String message) {
        MessageResponse messageText = new MessageResponse();
        messageText.setTo(-1);//-1 代表系统
        messageText.setCreateTime(new Date());
        messageText.setData(message);

        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(messageText)
        );
    }

    /**
     * 发送一条成功的用户消息
     *
     * @param message
     * @return
     */
    public static TextWebSocketFrame success(Integer to, Object message) {
        MessageResponse messageText = new MessageResponse();
        //messageText.setFrom(from);//服务器的响应信息无需设置发件人
        messageText.setCommandType(Command.ResponseCorrelation.getType());
        messageText.setTo(to);
        messageText.setCreateTime(new Date());
        messageText.setData(message);
        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(messageText));
    }

    /**
     * 发送一条成功的用户消息
     *
     * @param message
     * @return
     */
    public static TextWebSocketFrame success(Command command, Integer to, Object message) {
        MessageResponse messageText = new MessageResponse();
        //messageText.setFrom(from);//服务器的响应信息无需设置发件人
        messageText.setCommandType(command.getType());
        messageText.setTo(to);
        messageText.setCreateTime(new Date());
        messageText.setData(message);
        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(messageText));
    }

    /**
     * 发送私聊消息
     *
     * @param message
     * @return
     */
    public static TextWebSocketFrame privateMessage(Object message) {
        return new TextWebSocketFrame(
                JSONUtils.toJsonStr(message));
    }
}

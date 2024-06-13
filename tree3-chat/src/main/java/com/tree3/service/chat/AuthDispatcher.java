package com.tree3.service.chat;

import com.tree3.exception.BusinessException;
import com.tree3.pojo.command.Command;
import com.tree3.service.AuthService;
import com.tree3.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 连接/会话 相关的处理
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 15:33 </p>
 */
@Slf4j
@Component
public class AuthDispatcher {
    public void dispatch(Command match, ChannelHandlerContext context, TextWebSocketFrame frame) {
        log.debug("AuthDispatcher:{},{}", match, frame);
        AuthService authService = SpringUtil.getBean(AuthService.class);

        switch (match) {
            case Login:
                authService.login(context, frame);
                break;
            case Logout:
                authService.logout(context, frame);
                break;
            case Ping:
                authService.handlePing(context, frame);
                break;
            case Pong:
                authService.handlePong(context, frame);
                break;
            default:
                throw new BusinessException("不支持的单聊命令:" + frame.text());
        }
    }
}

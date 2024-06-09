package com.tree3.service.chat;

import com.tree3.exception.BusinessException;
import com.tree3.pojo.command.Command;
import com.tree3.service.PrivateChatService;
import com.tree3.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 处理私聊
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 15:31 </p>
 */
@Slf4j
@Component
public class PrivateChatDispatcher {

    public void dispatch(Command match, ChannelHandlerContext context, TextWebSocketFrame frame) {
        log.info("PrivateChatDispatcher：dispatch-->{}", match);
        PrivateChatService privateChatService = SpringUtil.getBean(PrivateChatService.class);
        switch (match) {
            case PrivateQueryHistory:
                privateChatService.queryChatHistory(context, frame);
                break;
            case PrivateChatText:
                privateChatService.transferChatMessage(context, frame);
                break;
            case ERROR:
                log.warn("PrivateChatDispatcher：dispatch-->ERROR-->{}", match);
                break;
            default:
                throw new BusinessException("不支持的单聊命令");
        }
    }

}

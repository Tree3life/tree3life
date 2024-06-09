package com.tree3.service.chat;

import com.tree3.pojo.command.Command;
import com.tree3.service.GroupChatService;
import com.tree3.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 处理群聊
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 15:31 </p>
 */
@Component
public class GroupChatDispatcher {

    public void dispatch(Command match, ChannelHandlerContext context, TextWebSocketFrame frame) {
        GroupChatService groupChatService = SpringUtil.getBean(GroupChatService.class);
        switch (match) {
            case GroupChatText:
                groupChatService.transferChatMessage(context, frame);
                break;
            case CreatChatGroup:
                break;
            case EditChatGroup:
                break;
            case DeleteChatGroup:
                break;

            case JoinChatGroup:
                break;
            case QuitChatGroup:
                break;

            case AddChatGroupMembers:
                break;
            case RemoveChatGroupMembers:
                break;
            default:
                break;
        }
    }
}

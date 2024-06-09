package com.tree3.pojo;

import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.message.MessageText;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/24 10:59 </p>
 */
public class PojoConvertor {
    public static ChatHistory convertTOChatHistory(MessageText msg) {
        ChatHistory chatHistory = new ChatHistory();
//        chatHistory.setId();
        chatHistory.setState(msg.getState());
        chatHistory.setCommand(msg.getCommandType());
        chatHistory.setState(msg.getState());
        chatHistory.setFrom(msg.getFrom());
        chatHistory.setTo(msg.getTo());
        chatHistory.setContent(msg.getContent());
        chatHistory.setCreateTime(msg.getCreateTime());
        chatHistory.setDeliveryTime(msg.getDeliveryTime());
//        chatHistory.setUpdateTime();
//        chatHistory.setContentType();
//        chatHistory.setDeletedTime();
//        chatHistory.setDeleted(msg);
        return chatHistory;
    }
}

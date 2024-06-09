package com.tree3.service.impl;

import com.tree3.dao.ChatHistoryMapper;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.message.MessageText;
import com.tree3.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rupert
 * @since 2024-05-20 16:19:14
 */
@Slf4j
@Service("chatHistoryService")
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatHistoryMapper chatHistoryMapper;

    @Autowired
    public ChatHistoryServiceImpl(ChatHistoryMapper chatHistoryMapper) {
        this.chatHistoryMapper = chatHistoryMapper;
    }

    @Override
    public List<ChatHistory> queryAll() {
        return chatHistoryMapper.queryChatHistorys();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatHistory> queryAll(ChatHistory chatHistory, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return chatHistoryMapper.queryChatHistorys(chatHistory, offset, pageSize);
    }


    @Override
    public ChatHistory queryChatHistory(ChatHistory chatHistory) {
        return chatHistoryMapper.queryChatHistory(chatHistory);
    }


    @Override
    public ChatHistory queryById(Integer id) {
        return chatHistoryMapper.queryById(id);
    }

    @Override
    public ChatHistory update(ChatHistory chatHistory) {
        this.chatHistoryMapper.update(chatHistory);
        return queryById(chatHistory.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatHistoryMapper.deleteById(id) > 0;
    }

    @Override
    public ChatHistory insert(ChatHistory chatHistory) {
        this.chatHistoryMapper.insert(chatHistory);
        return chatHistory;
    }

    @Override
    public ChatHistory savePrivateChatTextMessage(MessageText message) {

        return null;
    }
}

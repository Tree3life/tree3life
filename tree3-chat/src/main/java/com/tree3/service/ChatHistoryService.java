package com.tree3.service;

import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.message.MessageText;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-05-20 16:19:14
 */
public interface ChatHistoryService {

    List<ChatHistory> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param chatHistory 实例对象
     */
    List<ChatHistory> queryAll(ChatHistory chatHistory, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-05-20 16:19:14
     */
    ChatHistory queryChatHistory(ChatHistory chatHistory);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    ChatHistory queryById(Integer id);


    /**
     * 修改数据
     *
     * @param chatHistory 实例对象
     */
    ChatHistory update(ChatHistory chatHistory);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param chatHistory 实例对象
     */
    ChatHistory insert(ChatHistory chatHistory);

    ChatHistory savePrivateChatTextMessage(MessageText message);
}

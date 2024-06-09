package com.tree3.service;

import com.tree3.pojo.dto.ChatGroupDTO;
import com.tree3.pojo.entity.ChatGroup;
import com.tree3.pojo.qo.ChatGroupVo;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-05-20 16:19:30
 */
public interface ChatGroupService {

    List<ChatGroup> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param chatGroup 实例对象
     */
    List<ChatGroup> queryAll(ChatGroup chatGroup, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-05-20 16:19:30
     */
    ChatGroup queryChatGroup(ChatGroup chatGroup);

    /**
     * 查询 群 及其 聊天记录
     *
     * @param chatGroup
     * @return
     */
    List<ChatGroupDTO> queryChatGroupsWithHistory(ChatGroupVo chatGroup);

    List<ChatGroupDTO> queryChatGroupsWithLastMessage(ChatGroupVo chatGroup);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    ChatGroup queryById(Integer id);


    /**
     * 修改数据
     *
     * @param chatGroup 实例对象
     */
    ChatGroup update(ChatGroup chatGroup);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param chatGroup 实例对象
     */
    ChatGroup insert(ChatGroup chatGroup);
}

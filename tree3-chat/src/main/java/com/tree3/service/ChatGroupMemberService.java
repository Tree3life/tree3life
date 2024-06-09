package com.tree3.service;

import com.tree3.pojo.entity.ChatGroupMember;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-05-21 08:54:21
 */
public interface ChatGroupMemberService {

    List<ChatGroupMember> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param chatGroupMember 实例对象
     */
    List<ChatGroupMember> queryAll(ChatGroupMember chatGroupMember, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-05-21 08:54:21
     */
    ChatGroupMember queryChatGroupMember(ChatGroupMember chatGroupMember);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    ChatGroupMember queryById(Integer id);


    /**
     * 修改数据
     *
     * @param chatGroupMember 实例对象
     */
    ChatGroupMember update(ChatGroupMember chatGroupMember);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param chatGroupMember 实例对象
     */
    ChatGroupMember insert(ChatGroupMember chatGroupMember);
}

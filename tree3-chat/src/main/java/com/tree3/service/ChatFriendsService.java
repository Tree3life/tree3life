package com.tree3.service;

import com.tree3.pojo.dto.ChatFriendDTO;
import com.tree3.pojo.entity.ChatFriends;
import com.tree3.pojo.qo.ChatFriendsQuery;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-05-20 16:19:51
 */
public interface ChatFriendsService {

    List<ChatFriends> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param chatFriends 实例对象
     */
    List<ChatFriends> queryAll(ChatFriends chatFriends, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-05-20 16:19:51
     */
    ChatFriends queryChatFriends(ChatFriends chatFriends);

    /**
     *
     *
     * @author rupert
     * @date 2024-05-18 09:00:06
     * @return
     */
    List<ChatFriendDTO> queryChatFriendsPro(ChatFriendsQuery chatFriends);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    ChatFriends queryById(Integer id);


    /**
     * 修改数据
     *
     * @param chatFriends 实例对象
     */
    ChatFriends update(ChatFriends chatFriends);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param chatFriends 实例对象
     */
    ChatFriends insert(ChatFriends chatFriends);
}

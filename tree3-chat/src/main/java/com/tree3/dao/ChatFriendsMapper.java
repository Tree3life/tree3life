package com.tree3.dao;

import com.tree3.pojo.dto.ChatFriendDTO;
import com.tree3.pojo.entity.ChatFriends;
import com.tree3.pojo.qo.ChatFriendsQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChatFriends 表数据库访问层
 *
 * @author Rupert
 * @since 2024-05-20 16:19:51
 */
@Mapper
public interface ChatFriendsMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ChatFriends queryById(Integer id);

    /**
     * 条件查询单个元素
     * !!当查询结果不唯一时 会抛出异常 org.apache.ibatis.exceptions.TooManyResultsException
     *
     * @throws
     * @author rupert
     * @date 2024-05-20 16:19:51
     * @see org.apache.ibatis.exceptions.TooManyResultsException
     */
    ChatFriends queryChatFriends(ChatFriends chatFriends);

    /**
     * 查找朋友 并返回两者 最近的一条聊天消息
     *
     * @param chatFriends
     * @return
     */
    List<ChatFriendDTO> queryChatFriendsWidthLastMsg(ChatFriendsQuery chatFriends);

    /**
     * 查找朋友 并返回两者 的聊天消息
     *
     * @param chatFriends
     * @return
     */
    List<ChatFriendDTO> queryChatFriendsWidthHistory(ChatFriends chatFriends);

    /**
     * 查询所有
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-05-20 16:19:51
     */
    List<ChatFriends> queryChatFriendss();


    /**
     * 实体作为筛选条件分页查询数据
     * 特别的-->查询所有：( null,  null,  null) 等价于 {@link ChatFriendsMapper#queryChatFriendss()}
     *
     * @param chatFriends 实例对象
     * @param offset      数据偏移量（从该条数据开始查询）
     * @param pageSize    数据条目的数量
     * @return 对象列表
     * @author rupert
     * @date 2024-05-20 16:19:51
     */
    List<ChatFriends> queryChatFriendss(ChatFriends chatFriends, Integer offset, Integer pageSize);

    /**
     * 统计总行数<br/>
     * 与
     * {@link ChatFriendsMapper#queryChatFriendss(ChatFriends chatFriends, Integer offset, Integer pageSize)}配合使用，完成分页查询
     *
     * @param chatFriends 查询条件
     * @return 总行数
     */
    long count(ChatFriends chatFriends);


    /**
     * 修改数据
     *
     * @param chatFriends 实例对象
     * @return 影响行数
     */
    int update(ChatFriends chatFriends);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * @param following 筛选条件
     * @return 影响行数
     */
    int deleteByChatFriends(ChatFriends chatFriends);

    /**
     * 通过主键逻辑删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int logicDeleteById(Integer id);

    /**
     * 新增数据
     *
     * @param chatFriends 实例对象
     * @return 影响行数
     */
    int insert(ChatFriends chatFriends);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatFriends> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("chatFriendss") List<ChatFriends> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatFriends> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("chatFriendss") List<ChatFriends> entities);

}


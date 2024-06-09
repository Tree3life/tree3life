package com.tree3.dao;

import com.tree3.pojo.dto.ChatHistoryDTO;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.qo.ChatHistoryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * ChatHistory 表数据库访问层
 *
 * @author Rupert
 * @since 2024-05-20 16:19:13
 */
@Mapper
public interface ChatHistoryMapper {
    /**
     * 统计 离线期间 某个好友 发来的消息的个数
     * @param chatHistory
     * @return
     */
    ArrayList<Integer> countUnreadMessage(ChatHistoryQuery chatHistory);
    boolean updateUnreadMessage(@Param("msgs") ArrayList<Integer> msgs,@Param("state") Integer state);

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ChatHistory queryById(Integer id);

    /**
     * 条件查询单个元素
     * !!当查询结果不唯一时 会抛出异常 org.apache.ibatis.exceptions.TooManyResultsException
     *
     * @throws
     * @author rupert
     * @date 2024-05-20 16:19:13
     * @see org.apache.ibatis.exceptions.TooManyResultsException
     */
    ChatHistory queryChatHistory(ChatHistory chatHistory);

    /**
     * 查询两个人之间的 最新的一条聊天记录
     *
     * @param userA
     * @param userB
     * @return
     */
    ChatHistory queryLastHistory(@Param("userA") Integer userA, @Param("userB") Integer userB, @Param("cmd") Integer cmd);

    /**
     * 查询两人之间的聊天记录
     *
     * @param userA
     * @param userB
     * @param cmd
     * @return
     */
    List<ChatHistory> queryFriendsChatHistory(@Param("userA") Integer userA, @Param("userB") Integer userB, @Param("cmd") Integer cmd);

    /**
     * 查询所有
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-05-20 16:19:13
     */
    List<ChatHistory> queryChatHistorys();


    /**
     * 实体作为筛选条件分页查询数据
     * 特别的-->查询所有：( null,  null,  null) 等价于 {@link ChatHistoryMapper#queryChatHistorys()}
     *
     * @param chatHistory 实例对象
     * @param offset      数据偏移量（从该条数据开始查询）
     * @param pageSize    数据条目的数量
     * @return 对象列表
     * @author rupert
     * @date 2024-05-20 16:19:13
     */
    List<ChatHistory> queryChatHistorys(ChatHistory chatHistory, Integer offset, Integer pageSize);

    List<ChatHistoryDTO> queryChatHistoryPro(ChatHistory chatHistory);

    /**
     * 查询群聊中的 最新一条消息
     *
     * @param chatHistory
     * @return
     */
    ChatHistory queryGroupLastChatHistory(ChatHistory chatHistory);

    /**
     * 统计总行数<br/>
     * 与
     * {@link ChatHistoryMapper#queryChatHistorys(ChatHistory chatHistory, Integer offset, Integer pageSize)}配合使用，完成分页查询
     *
     * @param chatHistory 查询条件
     * @return 总行数
     */
    long count(ChatHistory chatHistory);


    /**
     * 修改数据
     *
     * @param chatHistory 实例对象
     * @return 影响行数
     */
    int update(ChatHistory chatHistory);

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
    int deleteByChatHistory(ChatHistory chatHistory);

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
     * @param chatHistory 实例对象
     * @return 影响行数
     */
    int insert(ChatHistory chatHistory);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatHistory> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("chatHistorys") List<ChatHistory> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatHistory> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("chatHistorys") List<ChatHistory> entities);

}


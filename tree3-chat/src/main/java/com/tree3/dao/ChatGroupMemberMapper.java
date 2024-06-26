package com.tree3.dao;

import com.tree3.pojo.entity.ChatGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ChatGroupMember 表数据库访问层
 *
 * @author Rupert
 * @since 2024-05-21 08:54:20
 */
@Mapper
public interface ChatGroupMemberMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ChatGroupMember queryById(Integer id);

    /**
     * 条件查询单个元素
     * !!当查询结果不唯一时 会抛出异常 org.apache.ibatis.exceptions.TooManyResultsException
     *
     * @throws
     * @author rupert
     * @date 2024-05-21 08:54:20
     * @see org.apache.ibatis.exceptions.TooManyResultsException
     */
    ChatGroupMember queryChatGroupMember(ChatGroupMember chatGroupMember);


    /**
     * 查询所有
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-05-21 08:54:20
     */
    List<ChatGroupMember> queryChatGroupMembers();


    /**
     * 实体作为筛选条件分页查询数据
     * 特别的-->查询所有：( null,  null,  null) 等价于 {@link ChatGroupMemberMapper#queryChatGroupMembers()}
     *
     * @param chatGroupMember 实例对象
     * @param offset          数据偏移量（从该条数据开始查询）
     * @param pageSize        数据条目的数量
     * @return 对象列表
     * @author rupert
     * @date 2024-05-21 08:54:20
     */
    List<ChatGroupMember> queryChatGroupMembers(ChatGroupMember chatGroupMember, Integer offset, Integer pageSize);

    /**
     * 查询群聊的所有成员
     *
     * @param chatGroupMember
     * @return
     */
    List<ChatGroupMember> queryChatGroupMembersPro(ChatGroupMember chatGroupMember);

    /**
     * 统计总行数<br/>
     * 与
     * {@link ChatGroupMemberMapper#queryChatGroupMembers(ChatGroupMember chatGroupMember, Integer offset, Integer pageSize)}配合使用，完成分页查询
     *
     * @param chatGroupMember 查询条件
     * @return 总行数
     */
    long count(ChatGroupMember chatGroupMember);


    /**
     * 修改数据
     *
     * @param chatGroupMember 实例对象
     * @return 影响行数
     */
    int update(ChatGroupMember chatGroupMember);

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
    int deleteByChatGroupMember(ChatGroupMember chatGroupMember);

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
     * @param chatGroupMember 实例对象
     * @return 影响行数
     */
    int insert(ChatGroupMember chatGroupMember);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatGroupMember> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("chatGroupMembers") List<ChatGroupMember> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ChatGroupMember> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("chatGroupMembers") List<ChatGroupMember> entities);

}


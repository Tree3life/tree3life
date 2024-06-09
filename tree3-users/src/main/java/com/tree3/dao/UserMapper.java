package com.tree3.dao;

import com.tree3.pojo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * User 表数据库访问层
 *
 * @author Rupert
 * @since 2024-05-13 21:17:31
 */
@Mapper
public interface UserMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    User queryById(Integer id);
    User queryByIdInsensitive(Integer id);

    /**
     * 条件查询单个元素
     * !!当查询结果不唯一时 会抛出异常 org.apache.ibatis.exceptions.TooManyResultsException
     *
     * @throws
     * @author rupert
     * @date 2024-05-13 21:17:31
     * @see org.apache.ibatis.exceptions.TooManyResultsException
     */
    User queryUser(User user);


    /**
     * 查询所有
     *
     * @return 对象列表
     * @author rupert
     * @date 2024-05-13 21:17:31
     */
    List<User> queryUsers();


    /**
     * 实体作为筛选条件分页查询数据
     * 特别的-->查询所有：( null,  null,  null) 等价于 {@link UserMapper#queryUsers()}
     *
     * @param user     实例对象
     * @param offset   数据偏移量（从该条数据开始查询）
     * @param pageSize 数据条目的数量
     * @return 对象列表
     * @author rupert
     * @date 2024-05-13 21:17:31
     */
    List<User> queryUsers(User user, Integer offset, Integer pageSize);

    /**
     * 统计总行数<br/>
     * 与
     * {@link UserMapper#queryUsers(User user, Integer offset, Integer pageSize)}配合使用，完成分页查询
     *
     * @param user 查询条件
     * @return 总行数
     */
    long count(User user);


    /**
     * 修改数据
     *
     * @param user 实例对象
     * @return 影响行数
     */
    int update(User user);

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
    int deleteByUser(User user);

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
     * @param user 实例对象
     * @return 影响行数
     */
    int insert(User user);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<User> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("users") List<User> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<User> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("users") List<User> entities);

}


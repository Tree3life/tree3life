package com.tree3.service;

import com.tree3.pojo.entity.User;

import java.util.List;

/**
 * @author Rupert
 * @since 2024-05-13 21:17:31
 */
public interface UserService {

    List<User> queryAll();

    /**
     * 多条件选择查询：实体作为筛选条件查询数据
     *
     * @param user 实例对象
     */
    List<User> queryAll(User user, Integer page, Integer pageSize);

    /**
     * 条件查询单个元素
     *
     * @author rupert
     * @date 2024-05-13 21:17:31
     */
    User queryUser(User user);


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     */
    User queryById(Integer id);


    /**
     * 修改数据
     *
     * @param user 实例对象
     */
    User update(User user);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     */
    boolean deleteById(Integer id);

    /**
     * @param user 实例对象
     */
    User insert(User user);
}

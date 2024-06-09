package com.tree3.service.impl;

import com.tree3.dao.UserMapper;
import com.tree3.pojo.entity.User;
import com.tree3.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rupert
 * @since 2024-05-13 21:17:32
 */
@Slf4j
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> queryAll() {
        return userMapper.queryUsers();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<User> queryAll(User user, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return userMapper.queryUsers(user, offset, pageSize);
    }


    @Override
    public User queryUser(User user) {
        return userMapper.queryUser(user);
    }


    @Override
    public User queryById(Integer id) {
        return userMapper.queryById(id);
    }

    @Override
    public User update(User user) {
        this.userMapper.update(user);
        return queryById(user.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public User insert(User user) {
        this.userMapper.insert(user);
        return user;
    }
}

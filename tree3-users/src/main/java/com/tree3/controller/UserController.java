package com.tree3.controller;

import com.tree3.pojo.entity.User;
import com.tree3.service.UserService;
import com.tree3.sugar.boleresp.PackageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * User表控制器
 *
 * @author rupert
 * @since 2024-05-13 21:17:32
 */
@Slf4j
@RequestMapping("users")
@RestController
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/get/{id}")
    @PackageResponse
    public User queryById(@PathVariable("id") Integer id) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->queryById(Integer  " + id + ")");
        }

        return userService.queryById(id);
    }

    /**
     * 多条件选择查询
     *
     * @param user 查询条件：可自主设置为Vo对象
     * @return 响应结果
     */
    @GetMapping("/list")
    @PackageResponse
    public List<User> list(User user,
                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                           @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->list()：user：{}，page：{}，pageSize：{}",
                    user.toString(),
                    page,
                    pageSize);
        }

        return userService.queryAll(user, page, pageSize);
    }


    @PatchMapping("/update")
    @PackageResponse
    public User update(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->updateUser (" + user.toString() + ")");
        }

        return userService.update(user);
    }

    @DeleteMapping("/delete")
    @PackageResponse
    public Boolean deleteById(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->deleteById ( Integer  " + user.getId() + ") ");
        }

        return userService.deleteById(user.getId());
    }

    @PostMapping("/save")
    @PackageResponse
    public User save(@RequestBody User user) {
        if (log.isTraceEnabled()) {
            log.trace("UserController-->saveUser (" + user.toString() + ")");
        }

        return userService.insert(user);
    }

}

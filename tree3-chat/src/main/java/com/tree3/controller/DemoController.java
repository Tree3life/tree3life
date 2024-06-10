package com.tree3.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.tree3.client.UserClient;
import com.tree3.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 19:05 </p>
 */
@Slf4j
@RequestMapping("/chatT")
@RestController
public class DemoController {

    @Resource
    private UserClient userClient;

    @GetMapping("/aa")
    public String testMVC() {

        User user = new User();
        user.setUsername("上官");
        Object friends = userClient.findFriends(user);
        JSONObject jsonObject = JSONUtil.parseObj(friends);
        log.debug("friends:{}", friends);
        Object body = jsonObject.get("body");
        Object data = JSONUtil.parseObj(body).get("data");
        log.debug("body:{}", body);
        log.debug("data:{}", data);
        return "chatDemo";
    }
}

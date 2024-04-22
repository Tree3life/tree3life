package com.tree3.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 19:05 </p>
 */
@RequestMapping("usersT")
@RestController
public class TestController {
    @GetMapping("aa")
    public String testMVC() {
        System.out.println("usersT do");
        return "usersT";
    }
}

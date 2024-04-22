package com.tree3.controller;

import com.tree3.constants.CommunicationMessage;
import com.tree3.pojo.vo.LoginVo;
import com.tree3.sugar.boleresp.PackageResponse;
import com.tree3.sugar.boleresp.ResponseResultHelper;
import com.tree3.sugar.boleresp.ResponseResultInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/19 11:42 </p>
 */
@RestController
public class AuthController {


    @PackageResponse
    @PostMapping("/login")
    public Object login(@RequestBody LoginVo user) {
        return ResponseResultHelper.success(user, CommunicationMessage.Login_SUCCESS);
    }


    @PackageResponse
    @GetMapping("/logout")
    public Object logout() {
//        System.out.println(1/0);
        return ResponseResultHelper.successStr("aaa", "退出成功0.0");
    }

}

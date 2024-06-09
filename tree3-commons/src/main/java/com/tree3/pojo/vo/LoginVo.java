package com.tree3.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 16:17 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class LoginVo implements Serializable {
    private static final long serialVersionUID = 192832542147404064L;
    private String username;
    private String password;

    /**
     * 用于解决 同一个用户每登录一次就产生一个token的问题，
     * 前端在登录时尝试传递 存储在前端的token，如果本地存在token，将其传到后端，通知后端 删除该token
     */
//    private String token;
}

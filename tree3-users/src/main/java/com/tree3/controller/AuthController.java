package com.tree3.controller;

import cn.hutool.core.lang.UUID;
import com.alibaba.nacos.common.utils.Md5Utils;
import com.tree3.constants.CommunicationMessage;
import com.tree3.constants.RedisConstance;
import com.tree3.pojo.dto.UserDTO;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.vo.LoginVo;
import com.tree3.service.UserService;
import com.tree3.sugar.boleresp.PackageResponse;
import com.tree3.sugar.boleresp.ResponseResultHelper;
import com.tree3.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/19 11:42 </p>
 */
@Slf4j
@RestController
public class AuthController {
    @Autowired
    private RedisCache redisCache;

    @Value("${app.salt}")
    private String salt;

    @Autowired
    private UserService userService;

    @PackageResponse
    @PostMapping("/login")
    public Object login(HttpServletRequest request, @RequestBody LoginVo user) {

        try {
            log.debug("登录信息：{}", user);
            //参数校验
            if (ObjectUtils.isEmpty(user) || StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
                return ResponseResultHelper.fail(CommunicationMessage.ILLEGAL_PARAMETER);
            }

            //Step 1: 校验用户信息
            User formUser = new User();
            formUser.setUsername(user.getUsername());
            User dbUser = userService.queryUser(formUser);
            if (ObjectUtils.isEmpty(dbUser)) {
                return ResponseResultHelper.fail(CommunicationMessage.UNKNOWN_USER);
            }

            //检查密码是否一致!!注册时应当将 getCiphertext(user.getPassword()) 作为密码进行 数据库存储
            String ciphertext = getCiphertext(user.getPassword());
            if (!ciphertext.equals(dbUser.getPassword())) {
                return ResponseResultHelper.fail(CommunicationMessage.IncorrectPassword);
            }

            //2.1.随机生成token，作为登录令牌
            String token_suffix = UUID.randomUUID().toString(true);
            String tokenKey = RedisConstance.TOKEN_KEY + token_suffix;
            // 2.2.将User对象转为HashMap存储
;
            //去除敏感信息
            dbUser.setPassword(token_suffix);
            dbUser.setOpenid(null);

            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(dbUser, userDTO);

            redisCache.setCacheObject(tokenKey, userDTO, 24, TimeUnit.HOURS);
            log.debug("新登录的用户信息：token-{},info-{}", tokenKey, dbUser);
            //Step 3: 返回消息
            return ResponseResultHelper.success(dbUser, CommunicationMessage.Login_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResultHelper.fail(e.getMessage());
        }
    }


    @PackageResponse
    @GetMapping("/logout")
    public Object logout() {
//        System.out.println(1/0);
        return ResponseResultHelper.successStr("aaa", "退出成功0.0");
    }

    @PostMapping("/signIn")
    public Object signIn() {

        return null;
    }

    //    public String encryption(String password, String salt, int hashIterations) {
//        return new Md5Hash(password, salt, hashIterations).toHex();
//    }


    /**
     * 获取 密码的md5形式
     * 加密规则：md5(password+salt)
     * 例如：密码=123456，salt=tree3,
     * 返回值为：md5(123456+tree3,)=73f68f52d534ed300aaab14b464b5303
     *
     * @param text
     * @return 对 `password`和`salt`拼接后的字符串  进行 md5处理后的字符串
     */
    private String getCiphertext(String text) {
        return Md5Utils.getMD5(text + salt, StandardCharsets.UTF_8.name());
    }

    public static void main(String[] args) {
        System.out.println(Md5Utils.getMD5("2" + "上官" + "上官燕" + "19" + "tree3,", StandardCharsets.UTF_8.name()));
        LocalDate.now();
    }


}

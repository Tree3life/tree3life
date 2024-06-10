package com.tree3.client;

import com.tree3.pojo.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/16 18:24 </p>
 */

//@FeignClient(value="Tree3-Users",configuration = FeignConfiguration.class)
@FeignClient("Tree3-Users")
public interface UserClient {


    @GetMapping("/users/list")
//    List<User> findFriends(@SpringQueryMap User user);
    Object findFriends(@SpringQueryMap User user);

}

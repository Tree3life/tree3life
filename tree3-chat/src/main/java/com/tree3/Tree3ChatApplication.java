package com.tree3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/18 18:28 </p>
 */
@EnableFeignClients
@SpringBootApplication
public class Tree3ChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(Tree3ChatApplication.class);
    }
}

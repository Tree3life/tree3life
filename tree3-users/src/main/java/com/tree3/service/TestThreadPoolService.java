package com.tree3.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * 测试线程池的使用
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/29 23:11 </p>
 */
@Service
public class TestThreadPoolService {
    private static final Logger log = LoggerFactory.getLogger(TestThreadPoolService.class);

    /**
     * 没有返回值的 线程池方法调用
     *
     * @param email
     * @param message
     */
    @Async("appThreadPool")//线程池Executor Bean对象的名称
    public void asyncANoneReturn(String email, String message) {
        try {
            Thread.sleep(new Random().nextInt(3000));
            log.debug("邮件：{} ，to {} 已发送", message, email);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池 调用 有返回值的方法
     *
     * @param email
     * @param message
     */
    @Async("appThreadPool")//线程池Executor Bean对象的名称
    public CompletableFuture<String> asyncBNoneReturn(String email, String message) {
        try {
            Thread.sleep(new Random().nextInt(3000));
            log.debug("邮件：{} ，to {} 已发送", message, email);
            return CompletableFuture.completedFuture("邮件to-" + email + " 已发送");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

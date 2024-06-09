package com.tree3.config.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 配置线程池
 * note：线程池的配置应当 按需添加到 需要线程池的模块中
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/29 22:49 </p>
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    /**
     * 定义线程池
     *
     * @return
     */
//    @Bean("${spring.application.name}AppThreadPool")
    @Bean("appThreadPool")
//    public Executor appThreadPool() {
    public ThreadPoolTaskExecutor appThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数：线程池创建时候初始化的线程数
        executor.setCorePoolSize(10);

        //最大线程数=核心线程数+救急线程数：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(20);

        //任务队列的容量：用来暂存 待执行的任务 的队列
        executor.setQueueCapacity(500);

        //救急线程 空闲时间的阈值（秒），空闲时间 超过 该阈值 后 会对 救急线程 进行销毁
        executor.setKeepAliveSeconds(60);

        //设置线程池中 线程名称的前缀
        executor.setThreadNamePrefix("app-thread-pool");

        //任务队列的拒绝策略：当 核心线程、 任务队列、救急线程 三者同时被占用完时，仍然有新的线程到来时 应对这些超额任务的处理策略
        //由调用线程处理（一般是主线程）
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.DiscardPolicy()
        );

        //初始化线程池
        executor.initialize();
        return executor;
    }
}

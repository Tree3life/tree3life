package com.tree3.controller;

import com.tree3.constants.RedisConstance;
import com.tree3.exception.BusinessException;
import com.tree3.service.TestThreadPoolService;
import com.tree3.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.tree3.config.RabbitMQProducerConfig.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK;

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
    private static final Logger log = LoggerFactory.getLogger(TestController.class);
    @Autowired
    private TestThreadPoolService threadPoolService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("aa")
    public String testMVC() {
        System.out.println("usersT do");
        return "usersT";
    }


    @GetMapping("bbb")
    public void testRabbitMQSendMessage() {
        try {
            //将消息 msgTemp 发送到名为 EXCHANGE 的交换机，并使用路由键 "routerPath" 进行消息路由。根据交换机和队列的绑定关系，消息将被路由到与该路由键匹配的队列中，供消费者进行消费。
            String msgTemp = "mmmmmm";
//            for (int i = 0; i < 10; i++) {
//                rabbitTemplate.convertAndSend("", RabbitMQProducerConfig.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK, msgTemp + i, message -> {
//                    log.debug("MessagePostProcessor 消息发送以后。。。。。。。。。");
//                    /**
//                     * 设置消息持久化
//                     * MessageDeliveryMode.NON_PERSISTENT：非持久化。当消息被发送到 RabbitMQ 服务器后，如果服务器发生重启或崩溃，该消息可能会丢失。
//                     * MessageDeliveryMode.PERSISTENT：持久化消息。当消息被发送到 RabbitMQ 服务器后，会被持久化存储，即使服务器发生重启或崩溃，该消息也不会丢失。
//                     */
//                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                    return message;
//                });
//            }


            long msgIdentification = redisUtils.nextDistributedID(RedisConstance.RabbitMQ_MSGID);
            rabbitTemplate.convertAndSend("", TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK,
                    "{\"content\":\"测试xxx\"}",
                    new CorrelationData(msgIdentification + "")
            );
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试调用 线程池 执行 无返回值 方法
     *
     * @return
     */
    @GetMapping("aPool")
    public String testAThreadPool() {
        System.out.println("usersT do");
        String[] emails = new String[]{
                "a1@163.com",
                "a2@163.com",
                "a3@163.com",
                "a4@163.com",
                "a5@163.com",
        };
        for (int i = 0; i < emails.length; i++) {
            threadPoolService.asyncANoneReturn(emails[i], "测试使用线程池发送" + i);
        }
        return "usersT";
    }

    /**
     * 测试调用 线程池 执行 有返回值 方法
     *
     * @return
     */
    @GetMapping("bPool")
    public List<String> testBThreadPool() {

        CompletableFuture<String>[] futures = new CompletableFuture[5];
        String[] emails = new String[]{
                "a1@163.com",
                "a2@163.com",
                "a3@163.com",
                "a4@163.com",
                "a5@163.com",
        };

        for (int i = 0; i < emails.length; i++) {
            futures[i] = threadPoolService.asyncBNoneReturn(emails[i], "测试使用线程池发送" + i);
        }

        //阻塞等待线程的响应结果
        CompletableFuture.allOf(futures);

        List<String> resultStrs = Arrays.stream(futures).map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                throw new BusinessException("获取线程池运行结果失败");
            }
        }).collect(Collectors.toList());


        log.debug("================运行结果{}", resultStrs);
        return resultStrs;
    }


    /**
     * 测试调用 线程池 执行 有返回值 方法
     *
     * @return
     */
    @GetMapping("cPool")
    public String testCThreadPool() {
        CompletableFuture<String>[] futures = new CompletableFuture[5];
        String[] emails = new String[]{
                "a1@163.com",
                "a2@163.com",
                "a3@163.com",
                "a4@163.com",
                "a5@163.com",
        };

        for (int i = 0; i < emails.length; i++) {
            futures[i] = threadPoolService.asyncBNoneReturn(emails[i], "测试使用线程池发送" + i);
        }

        //阻塞等待所有 任务的执行结果
        CompletableFuture.allOf(futures);//代码会阻塞在此处

        StringBuilder builder = new StringBuilder("result=");

        for (int j = 0; j < futures.length; j++) {
            try {
                builder.append(futures[j].get().toString() + "\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        log.debug("================运行结果2{}", builder);

        return builder.toString();
    }

//    /**
//     * 测试调用 线程池 执行 有返回值 方法
//     *
//     * @return
//     */
//    @GetMapping("bPool")
//    public String testBThreadPool() throws ExecutionException, InterruptedException {
//        StringBuilder builder = new StringBuilder("result=");
//
//        CompletableFuture<String>[] futures = new CompletableFuture[5];
//        String[] emails = new String[]{
//                "a1@163.com",
//                "a2@163.com",
//                "a3@163.com",
//                "a4@163.com",
//                "a5@163.com",
//        };
//
//        for (int i = 0; i < emails.length; i++) {
//            futures[i] = threadPoolService.asyncBNoneReturn(emails[i], "测试使用线程池发送" + i);
//        }
//
//
//        //阻塞等待线程的响应结果
//        CompletableFuture<List<String>> futureResults = CompletableFuture.allOf(futures).thenApply((nullV) -> {
//            //遍历 futures获取各线程的执行结果
//            return Arrays.stream(futures).map(future -> {
//                try {
//                    return future.get();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//                throw new RuntimeException("获取消息失败");
//            }).collect(Collectors.toList());
//        });
//
//        List<String> strings = futureResults.get();
//        for (String string : strings) {
//            builder.append("\n" + string);
//        }
//
//
//        log.debug("================运行结果{}", builder);
//
//        return builder.toString();
//    }
}

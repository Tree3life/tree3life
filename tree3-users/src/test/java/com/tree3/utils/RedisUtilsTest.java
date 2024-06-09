package com.tree3.utils;

import com.tree3.Tree3UsersApplication;
import com.tree3.pojo.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/5 21:48 </p>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Tree3UsersApplication.class)
public class RedisUtilsTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void nextDistributedID() {
    }

    @Test
    public void set() {
    }

    @Test
    public void setWithLogicalExpire() {
        UserDTO value = new UserDTO();
        redisUtils.setWithLogicalExpire(
                "testLogicalExpire",
                value,
                5l,
                TimeUnit.SECONDS
        );
    }

    @Test
    public void getWithLogicalExpire() {
        UserDTO withLogicalExpire = redisUtils.getWithLogicalExpire(
                "testLogicalExpire",
                "testparam",
                UserDTO.class,
                (p) -> {
                    log.info("参数：{}", p);
                    log.info("根据参数，操作数据库/其它操作重建缓存 逻辑");
                    UserDTO userDTO = new UserDTO();
                    userDTO.setName("重建");
                    return userDTO;
                },
                30l,
                TimeUnit.SECONDS
        );
        log.info("查询：testLogicalExpire-->{}", withLogicalExpire);
    }

    @Test
    public void getWithCachePenetration() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("p1", "重建缓存时使用到的参数");
        params.put("p2", 2);

        UserDTO userDTO = redisUtils.getWithCachePassThrough(
                "testCachePenetration",
                params,
                UserDTO.class,
                (p) -> {
                    StringBuilder builder = new StringBuilder();

                    //构建缓存时 用到的所有参数
                    p.values().stream().forEach(i -> {
                        builder.append(String.valueOf(i) + "&");
                    });

                    log.info("防止缓存穿透:{}", builder.toString());
                    UserDTO dto = new UserDTO();
                    dto.setName("尝试查询数据库");
//                    return dto;
                    return null;
                },
                10l,
                TimeUnit.SECONDS
        );
        log.info("带有 解决缓存穿透能力的 获取方法,拿到的数据：{}", userDTO);
    }

//    @Test
//    public void nextDistributedID() {
//        CountDownLatch latch = new CountDownLatch(300);
//
//        //构建任务
//        Runnable task = () -> {
//            for (int i = 0; i < 100; i++) {
//                long id = redisUtils.nextDistributedID("order");
//                System.out.println("id = " + id);
//            }
//            latch.countDown();
//        };
//        long begin = System.currentTimeMillis();
//        for (int i = 0; i < 300; i++) {
//            //将任务交给线程池进行执行
//            es.submit(task);
//        }
//
//        //阻塞等待线程池将300个任务(获取分布式ID)执行完
//        latch.await();
//        long end = System.currentTimeMillis();
//        //计算耗时
//        System.out.println("time = " + (end - begin));
//    }

}
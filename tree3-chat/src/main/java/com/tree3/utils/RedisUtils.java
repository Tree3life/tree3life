package com.tree3.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.tree3.utils.RedisUtils.Constant.*;

/**
 * <p>
 * Redis在项目中的应用
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/5 21:22 </p>
 */
@Slf4j
@Component
public class RedisUtils {
    // todo：Redis在项目中的应用 (Rupert，2024/6/5 21:23)
    static {

    }

    /**
     * ！！！！:如果项目中配置了线程池 ，可以使用 项目中自带的线程池 appThreadPool
     * 否则使用一个固定大小的线程池
     * private ExecutorService appThreadPool = Executors.newFixedThreadPool(10);
     */
    @Resource(name = "appThreadPool")
    private ThreadPoolTaskExecutor appThreadPool;

    /**
     * 选定的参照点【2024/6/5 21:40】 距 1970-01-01T00:00:00Z 的毫秒值
     */
    private final long REFERENCE_POINT = 1717623600L;
    @Resource
    public RedisTemplate redisTemplate;

    @Resource
    public StringRedisTemplate strRedisTemplate;

    /**
     * 借鉴雪花算法
     * 生成分布式情况下的全局唯一ID
     * 0                  1-31         32-63
     * part1符号位（忽略）  part2时间戳   part3序列号
     * <p>
     * 序列号key的规则：由redis中的键值为 【increase:业务前缀:2024:06:05】
     *
     * @param keyBusinessPrefix 业务前缀
     * @return 分布式下 全局唯一ID
     */
    public long nextDistributedID(String keyBusinessPrefix) {
        LocalDateTime now = LocalDateTime.now();

        //结合本地日期时间和指定的偏移量 将 给定的日期时间 转换 为从1970-01-01T00:00:00Z开始的秒数。
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        //timestamp = 当前时间的毫秒值-`1970-01-01T00:00:00Z`对应的毫秒值
        long part2 = nowSecond - REFERENCE_POINT;

        //Step 1.获取序列号
        //Step 1.1构建/获取redis中存取序列号的key
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //Step 1.2根据key获取到当天的序列号
        long part3 = strRedisTemplate.opsForValue().increment(Constant.Prefix_INCREMENT + keyBusinessPrefix + SEPARATOR + date);

        //Step 2.将时间戳和序列号进行拼接
        return part2 << Constant.ID_PART3_BITS | part3;
    }

    /**
     * 将任意Java对象序列化为json并存储在string类型的key中，<br/>
     * 可设置TTL过期时间
     *
     * @param key
     * @param value
     * @param expire 过期时间
     * @param unit
     */
    public void set(String key, Object value, long expire, TimeUnit unit) {
        strRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), expire, unit);
    }

    /**
     * 将任意Java对象序列化为json并存储在string类型的key中，<br/>
     * 可<strong>设置逻辑过期时间<strong/>
     *
     * @param bizKey 业务key，逻辑的失效真正的键值 由{@link Constant .Prefix_LogicalExpire} +bizKey 构成
     * @param value
     * @param time
     * @param unit
     */
    public void setWithLogicalExpire(String bizKey, Object value, Long time, TimeUnit unit) {
        // 设置逻辑过期
        RedisDTO redisDTO = new RedisDTO();
        redisDTO.setData(value);
        redisDTO.setExpireTime(LocalDateTime.now().plusSeconds(unit.toSeconds(time)));

        String key = Prefix_LogicalExpire + bizKey;
        // 写入Redis
        strRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(redisDTO));
    }

    /**
     * 获取使用逻辑过期时间的 数据
     *
     * @param bizKey
     * @param param
     * @param resultClass
     * @param dbFallback
     * @param time
     * @param unit
     * @param <Result>
     * @param <Param>
     * @return
     */
    public <Result, Param> Result getWithLogicalExpire(
            String bizKey, Param param, Class<Result> resultClass, Function<Param, Result> dbFallback, Long time, TimeUnit unit) {
        String key = Prefix_LogicalExpire + bizKey;
        // 1.从redis查询缓存数据
        String json = strRedisTemplate.opsForValue().get(key);
        // 2.数据并未进行缓存
        if (StrUtil.isBlank(json)) {
            // 3.直接返回
            log.warn("数据：{} 并未进行缓存", bizKey);
            return null;
        }

        // 4.存在，
        // 4.1 把json反序列化为对象
        RedisDTO redisDTO = JSONUtil.toBean(json, RedisDTO.class);

        Result result = JSONUtil.toBean((JSONObject) redisDTO.getData(), resultClass);

        // 4.2.判断是否过期
        LocalDateTime expireTime = redisDTO.getExpireTime();
        if (expireTime.isAfter(LocalDateTime.now())) {
            // 4.2.1 未过期，成功返回数据
            return result;
        }
        // 4.2.2=5 已过期，需要缓存重建

        // 5.缓存重建
        // 5.1.获取互斥锁
        String lockKey = Constant.Prefix_LOCK_KEY + bizKey;
        boolean isLock = tryLock(lockKey);
        // 5.2.判断是否获取锁成功
        if (isLock) {
            // 6.3.成功，开启独立线程，实现缓存重建
            log.debug("缓存重建：{}", bizKey);
            appThreadPool.submit(() -> {
                try {
                    // 查询数据库
                    Result newR = dbFallback.apply(param);
                    // 重建缓存
                    this.setWithLogicalExpire(bizKey, newR, time, unit);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放锁
                    unlock(lockKey);
                }
            });
        }
        // 5.4.返回过期的商铺信息
        return result;
    }


    /**
     * 带有 解决缓存穿透能力 的 查询<br/>
     * 可利用 此方法 避免/解决缓存击穿问题
     * <p>示例：<br/>
     * redisUtils.getWithCachePenetration(
     * CACHE_SHOP_KEY,
     * id,
     * Shop.class,
     * (p, re) -> {
     * return null;
     * },
     * CACHE_SHOP_TTL,
     * TimeUnit.MINUTES);
     * </p>
     *
     * @param key         键值
     * @param param       重建缓存时使用到的参数，用于从数据库查询数据的参数
     * @param resultClass 返回结果的类型
     * @param dbCallback  重建缓存的业务逻辑
     * @param time        存活时间
     * @param unit        时间单位
     * @param <Result>
     * @param <Param>
     * @return
     */
    public <Result, Param> Result getWithCachePassThrough(
            String key, Param param, Class<Result> resultClass, Function<Param, Result> dbCallback, Long time, TimeUnit unit) {
//        String key = ;
        // 1.根据key 从redis中查询 目标 缓存
        String json = strRedisTemplate.opsForValue().get(key);
        // 2.判断是否从缓存中读取到了数据
        if (StrUtil.isNotBlank(json)) {
            // 3.查到了数据，直接返回
            return JSONUtil.toBean(json, resultClass);
        }

        // 判断查询到的数据 是否是空值
        if (json != null) {
            // 返回一个错误信息
            return null;
        }

        //模板方法模式
        // 4.执行到此处 表明未能从缓存中读取到数据，尝试根据参数查询数据库
        Result result = dbCallback.apply(param);

        // 5.未从数据库中查询到 数据，返回错误
        if (result == null) {
            log.warn("缓存和数据库中均未找到目标数据：{},将空值写入redis,防止缓存穿透", key);
            // 将空值写入redis
            strRedisTemplate.opsForValue().set(key, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
            // 返回错误信息
            return null;
        }
        // 6.从数据库中查到了 数据，写入redis
        this.set(key, result, time, unit);
        return result;
    }


    /**
     * 获取key (过程中利用了互斥锁)
     * 可利用此方法 避免/解决缓存击穿问题
     *
     * @param bizKey
     * @param params
     * @param type
     * @param dbFallback
     * @param time
     * @param unit
     * @param <Result>
     * @param <Params>
     * @return
     */
    public <Result, Params> Result getWithMutex(
            String bizKey, Params params, Class<Result> type, Function<Params, Result> dbFallback, Long time, TimeUnit unit) {
        // 1.查询缓存
        String jsonData = strRedisTemplate.opsForValue().get(bizKey);
        // 2.判断是否从缓存中查到数据
        if (StrUtil.isNotBlank(jsonData)) {
            // 3.查到，直接返回
            return JSONUtil.toBean(jsonData, type);
        }
        // 判断从缓存中查到数据 是否是空值
        if (jsonData != null) {
            // 返回一个错误信息
            log.warn("查询的目标数据，并未缓存！！！");
            return null;
        }

        // 4.实现缓存重建
        // 4.1.获取互斥锁
        String lockKey = Prefix_LOCK_KEY + bizKey;
        Result result = null;
        try {
            boolean isLock = tryLock(lockKey);
            // 4.2.判断是否获取成功
            if (!isLock) {
                // 4.3.获取锁失败，休眠并重试
                Thread.sleep(50);
                return getWithMutex(bizKey, params, type, dbFallback, time, unit);
            }
            // 4.4.获取锁成功，根据id查询数据库
            result = dbFallback.apply(params);
            // 5.不存在，返回错误
            if (result == null) {
                // 将空值写入redis
                strRedisTemplate.opsForValue().set(bizKey, "", CACHE_NULL_TTL, TimeUnit.MINUTES);
                // 返回错误信息
                return null;
            }
            // 6.存在，写入redis
            this.set(bizKey, result, time, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 7.释放锁
            unlock(lockKey);
        }
        // 8.返回
        return result;
    }

    /**
     * 利用redis setnx命令的特性 模拟并获取锁
     *
     * @param key
     * @return
     */
    private boolean tryLock(String key) {
        Boolean flag = strRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    /**
     * 释放锁
     *
     * @param key
     * @return
     */
    private void unlock(String key) {
        strRedisTemplate.delete(key);
    }

    /**
     * 项目中用到的 Redis相关的常量
     */
    public class Constant {
        /**
         * 序列号的位数
         */
        private static final int ID_PART3_BITS = 32;
        /**
         * redis中能够实现对key进行分层的分隔符
         */
        public final static String SEPARATOR = ":";
        /**
         * 全局唯一ID前缀
         */
        public final static String Prefix_INCREMENT = "increase:";

        /**
         * 逻辑失效的键值前缀
         */
        public final static String Prefix_LogicalExpire = "logicalExpire:";

        /**
         * 锁的键值前缀
         */
        public final static String Prefix_LOCK_KEY = "lockKey:";
        public final static long CACHE_NULL_TTL = 10l;
    }


    /**
     * <p>
     *
     * </p>
     * <a>@Author: Rupert</ a>
     * <p>创建时间: 2024/6/7 6:47 </p>
     */
    @Data
    @NoArgsConstructor
    public class RedisDTO {
        private Object data;
        private LocalDateTime expireTime;
    }


}

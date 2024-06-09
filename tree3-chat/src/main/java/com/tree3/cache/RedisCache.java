package com.tree3.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 将所有的Redis查询缓存作为封装到本接口中
 * 本接口事实上 与 dao的地位相同
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/5 19:27 </p>
 */
public interface RedisCache {
    // optimized：将所有 需要使用 redis 做查询缓存的方法 抽取到此处 (Rupert，2024/6/5 )
    //
    public boolean cacheChatHistory(List<Object> history, Integer time, TimeUnit timeUnit);
}

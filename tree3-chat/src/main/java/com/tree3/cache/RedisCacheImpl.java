package com.tree3.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/6/5 19:31 </p>
 */
public class RedisCacheImpl implements RedisCache{
    @Override
    public boolean cacheChatHistory(List<Object> history, Integer time, TimeUnit timeUnit) {

        return false;
    }
}

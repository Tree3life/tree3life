---
--- DateTime: 2024/6/7 17:29
---
--比较当前线程的标识 与 缓存的锁中的标识 是否一致
if (redis.call('get', KEYS[1]) == ARGV[1]) then
    -- 释放锁
    return redis.call('del',KEYS[1])
end

return 0
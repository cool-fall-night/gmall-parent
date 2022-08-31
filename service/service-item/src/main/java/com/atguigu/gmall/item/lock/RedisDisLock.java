package com.atguigu.gmall.item.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 使用Redis实现的分布式锁
 *
 * @author 毛伟臣
 * 2022/8/31
 * 18:53
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class RedisDisLock {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 加锁
     *
     * @return 锁值token
     */
    public String lock() {

        String token = UUID.randomUUID().toString();
        //1、加锁(携带token)
        //2、设置过期时间
        //3、SETNXEX 在redis存值时添加过期时间和空时添加

        while (!redisTemplate.opsForValue().setIfAbsent("lock", token, 10, TimeUnit.SECONDS)) {
            //自旋阻塞式加锁
        }
        //加锁成功后返回token
        return token;
    }

    /**
     * 解锁
     * @param token 锁值token
     */
    public void unlock(String token) {
//        解锁的lua脚本
//         if redis.call("get",KEYS[1]) == ARGV[1]
//         then
//            return redis.call("del",KEYS[1]);
//         else
//            return 0;
//         end
        String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1]  then return redis.call('del',KEYS[1]); else  return 0; end";
        //执行脚本
        redisTemplate.execute(new DefaultRedisScript<>(luaScript,Long.class), Arrays.asList("lock"),token);
    }
}
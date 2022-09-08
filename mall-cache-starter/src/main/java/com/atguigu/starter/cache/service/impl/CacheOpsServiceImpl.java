package com.atguigu.starter.cache.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.starter.cache.service.CacheOpsService;
import com.atguigu.starter.cache.util.Jsons;
import com.fasterxml.jackson.core.type.TypeReference;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 毛伟臣
 * 2022/8/31
 * 22:42
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class CacheOpsServiceImpl implements CacheOpsService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    //专门执行延迟任务的线程池
    ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(4);

    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {

        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        if (RedisConst.NULL_VAL.equals(jsonStr)) {
            return null;
        }
        return Jsons.toObj(jsonStr, clz);
    }

    @Override
    public <T> T getCacheData(String cacheKey, Type type) {
        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        if (RedisConst.NULL_VAL.equals(jsonStr)) {
            return null;
        }
        return Jsons.toObj(jsonStr, new TypeReference<T>() {
            @Override
            public Type getType() {
                //返回携带泛型的type
                return type;
            }
        });
    }

    @Override
    public Boolean bloomContains(Object skuId) {

        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKUID);

        return filter.contains(skuId);

    }

    @Override
    public Boolean bloomContains(String bloomName, Object bloomValue) {
        RBloomFilter<Object> filter = redissonClient.getBloomFilter(bloomName);

        return filter.contains(bloomValue);
    }

    @Override
    public void saveData(String cacheKey, Object fromRpc) {

        if (fromRpc == null){
            //空值缓存
            redisTemplate.opsForValue().set(cacheKey, RedisConst.NULL_VAL,RedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        }else {
            //有值缓存
            String cacheJson = Jsons.toJsonStr(fromRpc);
            assert cacheJson != null;
            redisTemplate.opsForValue().set(cacheKey, cacheJson,RedisConst.SKU_DETAIL_TTL,TimeUnit.SECONDS);
        }
    }

    @Override
    public void saveData(String cacheKey, Object fromRpc, Long dataTtl) {
        if (fromRpc == null){
            //空值缓存
            redisTemplate.opsForValue().set(cacheKey, RedisConst.NULL_VAL,RedisConst.NULL_VAL_TTL, TimeUnit.SECONDS);
        }else {
            //有值缓存
            String cacheJson = Jsons.toJsonStr(fromRpc);
            assert cacheJson != null;
            redisTemplate.opsForValue().set(cacheKey, cacheJson,dataTtl,TimeUnit.SECONDS);
        }
    }

    @Override
    public void delay2Delete(String cacheKey) {

        redisTemplate.delete(cacheKey);

        scheduledThreadPool.schedule(()->{
            redisTemplate.delete(cacheKey);
        },5,TimeUnit.SECONDS);



    }

    @Override
    public Boolean tryLock(Long skuId) {

        //定义每个商品专用的锁的key
        String lockKey = RedisConst.LOCK_SKU_DETAIL + skuId;

        RLock lock = redissonClient.getLock(lockKey);
        //尝试加锁
        return lock.tryLock();
    }

    @Override
    public void unLock(Long skuId) {

        String lockKey = RedisConst.LOCK_SKU_DETAIL + skuId;

        RLock lock = redissonClient.getLock(lockKey);

        lock.unlock();
    }

    @Override
    public Boolean tryLock(String lockName) {
        //定义每个商品专用的锁的key

        RLock lock = redissonClient.getLock(lockName);
        //尝试加锁
        return lock.tryLock();
    }

    @Override
    public void unLock(String lockName) {

        RLock lock = redissonClient.getLock(lockName);

        lock.unlock();
    }
}

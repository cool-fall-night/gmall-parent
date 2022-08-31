package com.atguigu.gmall.item.cache.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    @Override
    public <T> T getCacheData(String cacheKey, Class<T> clz) {

        String jsonStr = redisTemplate.opsForValue().get(cacheKey);
        if (RedisConst.NULL_VAL.equals(jsonStr)) {
            return null;
        }
        T t = Jsons.toObj(jsonStr, clz);
        return t;
    }

    @Override
    public Boolean bloomContains(Long skuId) {

        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKUID);

        boolean contains = filter.contains(skuId);

        return contains;

    }

    @Override
    public Boolean tryLock(Long skuId) {

        //定义每个商品专用的锁的key
        String lockKey = RedisConst.LOCK_SKU_DETAIL + skuId;

        RLock lock = redissonClient.getLock(lockKey);
        //尝试加锁
        boolean tryLock = lock.tryLock();
        return tryLock;
    }

    @Override
    public void saveData(String cacheKey, Object fromRpc) {

        if (fromRpc == null){
            //空值缓存
            redisTemplate.opsForValue().set(cacheKey, RedisConst.NULL_VAL,RedisConst.NULL_VAL_TTL,TimeUnit.SECONDS);
        }else {
            //有值缓存
            String cacheJson = Jsons.toJsonStr(fromRpc);
            redisTemplate.opsForValue().set(cacheKey, cacheJson,RedisConst.SKU_DETAIL_TTL,TimeUnit.SECONDS);
        }
    }

    @Override
    public void unLock(Long skuId) {

        String lockKey = RedisConst.LOCK_SKU_DETAIL + skuId;

        RLock lock = redissonClient.getLock(lockKey);

        lock.unlock();
    }
}

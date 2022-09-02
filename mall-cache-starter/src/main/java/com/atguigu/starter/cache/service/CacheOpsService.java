package com.atguigu.starter.cache.service;

import java.lang.reflect.Type;

/**
 * @author 毛伟臣
 * 2022/8/31
 * 22:41
 * @version 1.0
 * @since JDK1.8
 */

public interface CacheOpsService {

    /**
     * 缓存中获取JSON并转化为普通对象
     */
    <T> T getCacheData(String cacheKey, Class<T> clz);

    /**
     * 缓存中获取JSON并转化为精确泛型对象
     * @param cacheKey
     * @param type
     * @return
     */
    <T> T getCacheData(String cacheKey, Type type);

    /**
     * 询问布隆中是否存在
     * @param skuId
     * @return
     */
    Boolean bloomContains(Object skuId);

    /**
     * 询问布隆中是否存在指定值
     * @param bloomName
     * @param bloomValue
     * @return
     */
    Boolean bloomContains(String bloomName, Object bloomValue);

    /**
     * 加分布式tryLock锁
     * @param skuId
     * @return
     */
    Boolean tryLock(Long skuId);

    /**
     * 解锁
     * @param skuId
     */
    void unLock(Long skuId);

    /**
     * 加分布式tryLock锁
     * @param lockName
     * @return
     */
    Boolean tryLock(String lockName);

    /**
     * 解锁
     * @param lockName
     */
    void unLock(String lockName);

    /**
     * 使用指定的key在缓存中存入数据
     * @param cacheKey
     * @param fromRpc
     */
    void saveData(String cacheKey, Object fromRpc);

    /**
     * 使用指定的key在缓存中存入数据
     */
    void saveData(String cacheKey, Object fromRpc,Long dataTtl);

    /**
     * 延迟双删
     * @param cacheKey
     */
    void delay2Delete(String cacheKey);
}

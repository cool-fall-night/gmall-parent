package com.atguigu.gmall.item.cache;

import com.atguigu.gmall.model.to.SkuDetailTo;

/**
 * @author 毛伟臣
 * 2022/8/31
 * 22:41
 * @version 1.0
 * @since JDK1.8
 */

public interface CacheOpsService {

    /**
     * 缓存中查询
     * @param cacheKey
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getCacheData(String cacheKey, Class<T> clz);

    /**
     * 询问布隆中是否存在
     * @param skuId
     * @return
     */
    Boolean bloomContains(Long skuId);

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
     * 使用指定的key在缓存中存入数据
     * @param cacheKey
     * @param fromRpc
     */
    void saveData(String cacheKey, Object fromRpc);
}

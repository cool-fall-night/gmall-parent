package com.atguigu.gmall.product.bloom;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 19:03
 * @version 1.0
 * @since JDK1.8
 */

public interface BloomOpsService {

    /**
     * 手动重置布隆过滤器
     * @param bloomName
     */
    void rebuildBloom(String bloomName,BloomDataQueryService dataQueryService);

}

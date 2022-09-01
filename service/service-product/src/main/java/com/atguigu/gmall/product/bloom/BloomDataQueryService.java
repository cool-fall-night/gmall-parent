package com.atguigu.gmall.product.bloom;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 19:54
 * @version 1.0
 * @since JDK1.8
 */

public interface BloomDataQueryService {

    /**
     * 布隆过滤器查数据
     * @return List集合
     */
    List queryData();
}

package com.atguigu.gmall.common.constant;

/**
 * Redis常量配置类
 * set name admin
 * @author 毛伟臣
 */
public class RedisConst {

    public static final String NULL_VAL = "x";

    public static final String LOCK_SKU_DETAIL = "lock:sku:detail:";

    public static final Long NULL_VAL_TTL = 60 * 30L;

    public static final Long SKU_DETAIL_TTL = 7 * 24 * 60 * 60L;

    public static final String SKU_INFO_PREFIX = "sku:info:";

    public static final String BLOOM_SKUID = "bloom:skuid:";

    public static final String CACHE_CATEGORYS = "categorys";

    public static final String LOCK_PREFICX = "lock:";

    public static final int SEARCH_PAGE_SIZE = 8;

    public static final String SKU_DEFAULT_SORT = "asc";
    public static final String USER_INFO_PREFIX = "user:info:";
}

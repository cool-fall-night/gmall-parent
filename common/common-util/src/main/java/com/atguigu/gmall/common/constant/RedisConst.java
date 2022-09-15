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

    public static final Long BASE_HOT_SCORE = 100L;

    public static final Long HOT_SCORE_SIZE = 100L;

    public static final String SKU_HOTSCORE_PRE = "sku:hotscore:";

    public static final String USERID_HEADER = "userid";

    public static final String USERTEMPID_HEADER = "userTempId";

    public static final String CART_KEY_PREFIX = "cart:user:";

    public static final Long CART_MAX_SIZE = 200L;

    public static final Integer ITEM_MAX_SIZE = 200;

    public static final String ORDER_TEMP_TOKEN = "order:temptoken:";

    public static final String NO_STOCK = "0";

    public static final long EXPIRE_TIME = 30*60*1000;

    public static final long REFUNDABLE_TIME = 7*24*60*60*1000;
}

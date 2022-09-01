package com.atguigu.gmall.product.schedule;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.starter.cache.constant.RedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 定时任务重建布隆过滤器
 * @author 毛伟臣
 * 2022/9/1
 * 20:16
 * @version 1.0
 * @since JDK1.8
 */


@Service
public class RebuildBloomTask {

    @Autowired
    BloomOpsService bloomOpsService;

    @Autowired
    BloomDataQueryService bloomDataQueryService;

    @Scheduled(cron = "0 0 3 ? * 3")
    public void rebuildSkuBloom(){

        bloomOpsService.rebuildBloom(RedisConst.BLOOM_SKUID,bloomDataQueryService);

    }
}

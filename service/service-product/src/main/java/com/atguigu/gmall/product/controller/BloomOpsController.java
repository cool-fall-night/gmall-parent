package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.bloom.impl.SkuBloomDataQueryServiceImpl;
import com.atguigu.starter.cache.constant.RedisConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 布隆过滤器操作
 * @author 毛伟臣
 * 2022/9/1
 * 19:00
 * @version 1.0
 * @since JDK1.8
 */

@RestController
@RequestMapping("/admin/product")
public class BloomOpsController {

    @Autowired
    private BloomOpsService bloomOpsService;

    @Autowired
    private BloomDataQueryService bloomDataQueryService;


    @GetMapping("/rebuild/sku/now")
    public Result rebuildBloom(){

        String bloomName = RedisConst.BLOOM_SKUID;

        bloomOpsService.rebuildBloom(bloomName, bloomDataQueryService);

        return Result.ok();
    }

}

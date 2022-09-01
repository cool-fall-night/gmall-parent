package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.bloom.BloomOpsService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 19:04
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class BloomOpsServiceImpl implements BloomOpsService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void rebuildBloom(String bloomName,BloomDataQueryService bloomDataQueryService) {

        //获取久的布隆过滤器
        RBloomFilter<Object> oldBloomFilter = redissonClient.getBloomFilter(bloomName);

        //创建新的布隆过滤器，并初始化
        String newBloomName = bloomName + "_new";
        RBloomFilter<Object> newBloomFilter = redissonClient.getBloomFilter(newBloomName);
        newBloomFilter.tryInit(5000000,0.00001);

        //方法引用,遍历添加数据
        bloomDataQueryService.queryData().forEach(newBloomFilter::add);

        //切换布隆过滤器 TODO 使用Lua脚本完成切换
        oldBloomFilter.rename("dumpBloomFilter");
        newBloomFilter.rename(bloomName);

        //异步删除过时的布隆过滤器
        oldBloomFilter.deleteAsync();

    }
}

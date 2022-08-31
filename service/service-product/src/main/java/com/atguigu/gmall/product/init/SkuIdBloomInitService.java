package com.atguigu.gmall.product.init;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 0:39
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class SkuIdBloomInitService {

     @Autowired
     SkuInfoService skuInfoService;

     @Autowired
    RedissonClient redissonClient;

     @PostConstruct //当前组件创建时启动
     public void initSkuBloom(){

         List<Long> skuIds = skuInfoService.findAllSkuId();

         RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKUID);

         if (!filter.isExists()){
             filter.tryInit(5000000,0.00001);
         }

         skuIds.stream().forEach(r->{
             filter.add(r);
         });
     }
}

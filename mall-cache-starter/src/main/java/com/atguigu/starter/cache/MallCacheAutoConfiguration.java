package com.atguigu.starter.cache;

import com.atguigu.starter.cache.aspect.CacheAspect;
import com.atguigu.starter.cache.service.CacheOpsService;
import com.atguigu.starter.cache.service.impl.CacheOpsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author 毛伟臣
 * 2022/9/2
 * 2:26
 * @version 1.0
 * @since JDK1.8
 */

@EnableAspectJAutoProxy
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Configuration
public class MallCacheAutoConfiguration {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public CacheAspect cacheAspect(){
        return new CacheAspect();
    }

    @Bean
    public CacheOpsService cacheOpsService(){
        return new CacheOpsServiceImpl();
    }
}

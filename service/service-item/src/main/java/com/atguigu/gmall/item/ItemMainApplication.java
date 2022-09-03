package com.atguigu.gmall.item;

import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 23:03
 * @version 1.0
 * @since JDK1.8
 */

@EnableAspectJAutoProxy
@Import(RedissonAutoConfiguration.class)
@EnableThreadPool
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
public class ItemMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}

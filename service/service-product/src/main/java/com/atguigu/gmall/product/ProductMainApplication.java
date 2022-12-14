package com.atguigu.gmall.product;

import com.atguigu.gmall.common.config.RedissonAutoConfiguration;
import com.atguigu.gmall.common.config.Swagger2Config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 毛伟臣
 * 2022/8/22
 * 21:44
 * @version 1.0
 * @since JDK1.8
 */

@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.search")
@EnableScheduling
@Import({Swagger2Config.class, RedissonAutoConfiguration.class})
@MapperScan("com.atguigu.gmall.product.mapper")
@SpringCloudApplication
public class ProductMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductMainApplication.class, args);
    }
}

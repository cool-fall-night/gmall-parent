package com.atguigu.gmall.order;

import com.atguigu.gmall.annotation.EnableAppRabbit;
import com.atguigu.gmall.common.annotation.EnableAutoExceptionHandler;
import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 毛伟臣
 * 2022/9/12
 * 21:02
 * @version 1.0
 * @since JDK1.8
 */

@EnableAppRabbit
@EnableTransactionManagement
@EnableAutoExceptionHandler
@EnableAutoFeignInterceptor
@MapperScan("com.atguigu.gmall.order.mapper")
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.product",
                                    "com.atguigu.gmall.feign.cart",
                                    "com.atguigu.gmall.feign.user",
                                    "com.atguigu.gmall.feign.ware"})
@SpringCloudApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}

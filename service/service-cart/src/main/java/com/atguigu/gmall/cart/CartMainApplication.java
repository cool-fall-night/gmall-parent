package com.atguigu.gmall.cart;

import com.atguigu.gmall.common.annotation.EnableAutoExceptionHandler;
import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 毛伟臣
 * 2022/9/8
 * 18:15
 * @version 1.0
 * @since JDK1.8
 */

@EnableAutoFeignInterceptor
@EnableAutoExceptionHandler
@SpringCloudApplication
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.product")
public class CartMainApplication {

    public static void main(String[] args) {

        SpringApplication.run(CartMainApplication.class,args);

    }
}

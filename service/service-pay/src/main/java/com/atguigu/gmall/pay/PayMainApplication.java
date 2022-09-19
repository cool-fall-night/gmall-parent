package com.atguigu.gmall.pay;

import com.atguigu.gmall.common.annotation.EnableAutoExceptionHandler;
import com.atguigu.gmall.common.annotation.EnableAutoFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 毛伟臣
 * 2022/9/16
 * 18:28
 * @version 1.0
 * @since JDK1.8
 */
@EnableAutoExceptionHandler
@EnableAutoFeignInterceptor
@EnableFeignClients(basePackages = "com.atguigu.gmall.feign.order")
@SpringCloudApplication
public class PayMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayMainApplication.class,args);
    }

    //买家账号bbmfap9716@sandbox.com
}

package com.atguigu.gmall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 20:49
 * @version 1.0
 * @since JDK1.8
 */

@SpringCloudApplication
@EnableFeignClients(basePackages = {"com.atguigu.gmall.feign.product", "com.atguigu.gmall.feign.item"})
public class WebAllMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebAllMainApplication.class,args);
    }
}

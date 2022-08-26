package com.atguigu.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 23:03
 * @version 1.0
 * @since JDK1.8
 */

@SpringCloudApplication
@EnableFeignClients
public class ItemMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItemMainApplication.class,args);
    }
}

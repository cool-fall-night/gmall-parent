package com.atguigu.gmall.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author 毛伟臣
 * 2022/9/12
 * 21:02
 * @version 1.0
 * @since JDK1.8
 */


@MapperScan("com.atguigu.gmall.order.mapper")
@SpringCloudApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}

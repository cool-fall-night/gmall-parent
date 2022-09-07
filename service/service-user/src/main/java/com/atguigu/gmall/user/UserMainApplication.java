package com.atguigu.gmall.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author 毛伟臣
 * 2022/9/7
 * 13:22
 * @version 1.0
 * @since JDK1.8
 */

@SpringCloudApplication
@MapperScan("com.atguigu.gmall.user.mapper")
public class UserMainApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserMainApplication.class,args);
    }
}

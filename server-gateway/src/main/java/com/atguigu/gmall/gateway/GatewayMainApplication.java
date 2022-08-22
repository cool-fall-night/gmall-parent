package com.atguigu.gmall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author 毛伟臣
 * 2022/8/22
 * 20:44
 * @version 1.0
 * @since JDK1.8
 */
//@EnableCircuitBreaker --sentinel熔断降级注解
//@EnableDiscoveryClient --nacos服务发现注解
//@SpringBootApplication --cloud主启动类注解
@SpringCloudApplication
public class GatewayMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMainApplication.class,args);
    }
}

package com.atguigu.gmall.ware;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 库存系统
 * @author 毛伟臣
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.gmall"})
//@EnableDiscoveryClient
public class ServiceWareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceWareApplication.class, args);
	}

}

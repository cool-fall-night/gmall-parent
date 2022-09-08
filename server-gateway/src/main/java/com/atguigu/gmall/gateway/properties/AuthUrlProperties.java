package com.atguigu.gmall.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/7
 * 19:42
 * @version 1.0
 * @since JDK1.8
 */

@Data
@Component
@ConfigurationProperties(prefix = "app.auth")
public class AuthUrlProperties {

    //无需登录就可访问的资源
    List<String> noAuthUrl;
    //需要登录才可访问的资源
    List<String> loginAuthUrl;
    //拒绝通过浏览器访问的资源
    List<String> denyAuthUrl;
    //登录页面
    String loginPage;
}

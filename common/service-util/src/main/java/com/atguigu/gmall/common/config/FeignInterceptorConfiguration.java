package com.atguigu.gmall.common.config;

import com.atguigu.gmall.common.constant.RedisConst;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 毛伟臣
 * 2022/9/8
 * 19:17
 * @version 1.0
 * @since JDK1.8
 */

@Configuration
public class FeignInterceptorConfiguration {

    @Bean
    public RequestInterceptor userHeaderInterceptor(){


       return (template)->{
           ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
           //获取老请求
           HttpServletRequest request = attributes.getRequest();

           //用户id头添加到feign的新情求中
           String userId = request.getHeader(RedisConst.USERID_HEADER);
           template.header(RedisConst.USERID_HEADER,userId);

           //临时id也透传
           String userTempId = request.getHeader(RedisConst.USERTEMPID_HEADER);
           template.header(RedisConst.USERTEMPID_HEADER,userTempId);
       };
    }
}

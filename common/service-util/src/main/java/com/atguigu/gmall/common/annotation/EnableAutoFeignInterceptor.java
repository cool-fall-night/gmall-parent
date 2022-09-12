package com.atguigu.gmall.common.annotation;

import com.atguigu.gmall.common.config.FeignInterceptorConfiguration;
import com.atguigu.gmall.common.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 毛伟臣
 * 2022/8/28
 * 23:37
 * @version 1.0
 * @since JDK1.8
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignInterceptorConfiguration.class)
public @interface EnableAutoFeignInterceptor {
}

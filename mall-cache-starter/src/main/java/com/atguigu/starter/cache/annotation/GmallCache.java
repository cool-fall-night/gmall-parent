package com.atguigu.starter.cache.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 缓存注解
 * @author 毛伟臣
 * 2022/9/1
 * 20:45
 * @version 1.0
 * @since JDK1.8
 */
@Transactional
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GmallCache {

    String cacheKey() default "";

    String bloomName() default "";

    String bloomValue() default "";

    String lockName() default "";

}

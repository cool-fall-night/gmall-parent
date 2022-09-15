package com.atguigu.gmall.annotation;

import com.atguigu.gmall.rabbit.AppRabbitConfiguration;
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
@Inherited
@Import(AppRabbitConfiguration.class)
public @interface EnableAppRabbit {
}

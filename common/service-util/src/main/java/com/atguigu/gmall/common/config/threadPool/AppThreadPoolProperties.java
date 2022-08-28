package com.atguigu.gmall.common.config.threadPool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 毛伟臣
 * 2022/8/28
 * 22:53
 * @version 1.0
 * @since JDK1.8
 */

@Data
@ConfigurationProperties(prefix = "app.thread-pool")
public class AppThreadPoolProperties {

    private Integer core;
    private Integer max;
    private Integer queueSize;
    private Long keepAliveTime;

}

package com.atguigu.gmall.common.config.threadPool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * 线程池配置类
 *
 * @author 毛伟臣
 * 2022/8/28
 * 22:27
 * @version 1.0
 * @since JDK1.8
 */

@EnableConfigurationProperties(AppThreadPoolProperties.class)
@Configuration
public class AppThreadPoolAutoConfiguration {

    @Value("${spring.application.name}")
    String applicationName;


    @Autowired
    private AppThreadPoolProperties appThreadPoolProperties;

    /**
     * int corePoolSize,                    --核心线程数 cpu核心数
     * int maximumPoolSize,                 --最大线程数
     * long keepAliveTime,                  --线程存活时间
     * TimeUnit unit,                       --时间单位
     * BlockingQueue<Runnable> workQueue,   --阻塞队列（合理指定大小）
     * ThreadFactory threadFactory,         --线程工厂
     * RejectedExecutionHandler handler     --拒绝策略
     */
    @Bean
    public ThreadPoolExecutor coreExecutor() {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                appThreadPoolProperties.getCore(),
                appThreadPoolProperties.getMax(),
                appThreadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(appThreadPoolProperties.getQueueSize()),
                new ThreadFactory() {
                    int i = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(applicationName + "core-thread-[" + i++ + "]");
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        //数组与链表的区别
        //new ArrayBlockingQueue<>(10);  存储为连续空间
        //new LinkedBlockingQueue<>(10); 存储为不连续空间
        return executor;
    }
}

package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 毛伟臣
 * 2022/8/28
 * 23:05
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class ThreadPoolController {

    @Autowired
    ThreadPoolExecutor executor;

    @GetMapping("/close/pool")
    public Result closePool(){
        executor.shutdown(); //关闭线程池
        return Result.ok();
    }

    @GetMapping("/monitor/pool")
    public Result monitorThreadPool(){

        int poolSize = executor.getCorePoolSize();
        long taskCount = executor.getTaskCount();

        return Result.ok(poolSize + "====" + taskCount);
    }

}

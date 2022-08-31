package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 毛伟臣
 * 2022/8/31
 * 20:18
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class RedissonTest {


    @Autowired
    RedissonClient redissonClient;

    @Test
    void Test01(){
        System.out.println(redissonClient);
    }
}

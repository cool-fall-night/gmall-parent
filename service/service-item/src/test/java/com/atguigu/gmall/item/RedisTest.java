package com.atguigu.gmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author 毛伟臣
 * 2022/8/29
 * 22:20
 * @version 1.0
 * @since JDK1.8
 * 1、RedisAutoConfiguration
 *     给容器中放了 RedisTemplate<Object, Object> 和 StringRedisTemplate
 *     给redis存数据，都是k-v（v有很多类型）【string,jsonstring】
 *     StringRedisTemplate = RedisTemplate<String, String> ；
 *     给redis存数据，key是string，value序列化成字符串
 */

@SpringBootTest
public class RedisTest {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void saveTest(){

        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        ops.set("hello","world!");
        System.out.println("redis保存完成");

        String hello = ops.get("hello");
    }

}

package com.atguigu.gmall.order;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 21:59
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class RabbitTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void testMq(){
        rabbitTemplate.convertAndSend("hahax","h","123");
    }
}

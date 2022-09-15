package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.constant.MqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 22:16
 * @version 1.0
 * @since JDK1.8
 */

@Configuration
public class OrderEventMqConfiguration {

    /**
     * 项目启动时自动创建交换机
     * @return
     */
    @Bean
    public Exchange orderEventExchange(){

        return new TopicExchange(MqConst.EXCHANGE_ORDER_EVENT,true,false);
    }
    @Bean
    public Queue orderDelayQueue(){

        HashMap<String, Object> arguments = new HashMap<>();

//        arguments.put("x-message-ttl", 5000);
        arguments.put("x-message-ttl", RedisConst.EXPIRE_TIME);
        arguments.put("x-dead-letter-exchange",MqConst.EXCHANGE_ORDER_EVENT);
        arguments.put("x-dead-letter-routing-key",MqConst.RK_ORDER_DEAD);

        return new Queue(MqConst.QUEUE_ORDER_DELAY,true,false,false,arguments);
    }
    @Bean
    public Binding orderDelayQueueBinding(){

        return new Binding(MqConst.QUEUE_ORDER_DELAY,
                Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_CREATED,null);
    }
    @Bean
    public Queue orderDeadQueue(){

        return new Queue(MqConst.QUEUE_ORDER_DEAD,true,false,false);
    }
    @Bean
    public Binding orderDeadQueueBinding(){

        return new Binding(MqConst.QUEUE_ORDER_DEAD,
                Binding.DestinationType.QUEUE,
                MqConst.EXCHANGE_ORDER_EVENT,
                MqConst.RK_ORDER_DEAD,null);
    }
}

package com.atguigu.gmall.rabbit;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 21:36
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
@EnableRabbit
@Configuration
public class AppRabbitConfiguration {

    @Bean
    RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory){

        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template,connectionFactory);

        //感知消息是否投递到消息队列
        template.setReturnCallback((Message message,
                                    int replyCode,
                                    String replyText,
                                    String exchange,
                                    String routingKey)->{
            //消息没有投递到消息队列
            log.error("消息投递到队列失败，保存到数据库，{}",message);

        });

        //
        template.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause)->{

            assert correlationData != null;
            Message message = correlationData.getReturnedMessage();
            if (!ack){
                //信息进入队列，但交换机未收到
                log.error("消息投递到服务器失败，保存到数据库，{}",message);
            }
        });

        //设置重试器，默认重试3次
        template.setRetryTemplate(new RetryTemplate());

        return template;
    }
}

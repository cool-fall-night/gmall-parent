package com.atguigu.gmall.constant;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 22:21
 * @version 1.0
 * @since JDK1.8
 */

public class MqConst {

    //订单交换机名
    public static final String EXCHANGE_ORDER_EVENT = "order-event-exchange";
    //订单延迟队列
    public static final String QUEUE_ORDER_DELAY = "order-delay-queue";
    //订单路由键
    public static final String RK_ORDER_DEAD = "order.dead";

    public static final String RK_ORDER_CREATED = "order.created";

    public static final String QUEUE_ORDER_DEAD = "order-dead-queue";
}

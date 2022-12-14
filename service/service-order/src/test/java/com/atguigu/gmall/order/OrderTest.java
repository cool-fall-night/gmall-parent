package com.atguigu.gmall.order;

import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

/**
 * @author 毛伟臣
 * 2022/9/12
 * 22:35
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class OrderTest {

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Test
    public void testSearch(){

        orderInfoMapper.selectById(1);

    }

    @Test
    public void testInsert(){

        //TODO Caused by: java.lang.IllegalStateException: Insert statement does not support sharding table routing to multiple data nodes.
        OrderInfo info = new OrderInfo();
        info.setTotalAmount(new BigDecimal("2.1"));

        orderInfoMapper.insert(info);
    }
}

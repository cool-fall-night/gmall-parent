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

        OrderInfo orderInfo = orderInfoMapper.selectById(13L);
        System.out.println("orderInfo = " + orderInfo);

    }

    @Test
    public void testInsert(){

        OrderInfo info = new OrderInfo();
        info.setTotalAmount(new BigDecimal("2.1"));
        info.setUserId(13L);

        orderInfoMapper.insert(info);
    }
}

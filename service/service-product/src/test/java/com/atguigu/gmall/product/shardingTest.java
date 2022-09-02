package com.atguigu.gmall.product;

import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 毛伟臣
 * 2022/9/2
 * 20:04
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class shardingTest {

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Test
    void test01() {

        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark);
        BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark2);
        BaseTrademark baseTrademark3 = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark3);
        BaseTrademark baseTrademark4 = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark4);
        BaseTrademark baseTrademark5 = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark5);
        BaseTrademark baseTrademark6 = baseTrademarkMapper.selectById(4);
        System.out.println("baseTrademark = " + baseTrademark6);

    }

    @Test
    public void test02() {
        BaseTrademark baseTrademark = baseTrademarkMapper.selectById(4L);
        System.out.println(baseTrademark);


        baseTrademark.setTmName("小米");
        baseTrademarkMapper.updateById(baseTrademark);

        //改完后，再去查询，很可能查不到最新结果

        //让刚改完的下次查询强制走主库
        HintManager.getInstance().setWriteRouteOnly(); //强制走主库
        BaseTrademark baseTrademark2 = baseTrademarkMapper.selectById(4L);
        System.out.println("改完后查到的是：" + baseTrademark2);

    }
}

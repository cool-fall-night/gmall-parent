package com.atguigu.gmall.item;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

/**
 * @author 毛伟臣
 * 2022/8/30
 * 0:42
 * @version 1.0
 * @since JDK1.8
 */

public class BloomTest {


    @Test
    public void testBloom(){
        /**
         funnel – the funnel of T's that the constructed BloomFilter will use
         expectedInsertions – the number of expected insertions to the constructed BloomFilter; must be positive
         fpp – the desired false positive probability (must be positive and less than 1.0)

        BloomFilter.create((from,into)->{
            into.putLong(Long.parseLong(from.toString()));
        },10000,0.0001);
        */
        //1.创建布隆过滤器
        BloomFilter<Long> filter = BloomFilter.create(Funnels.longFunnel(), 10000, 0.0001);
        //2.添加数据
        for (long i = 0; i < 20; i++) {
            filter.put(i);
        }
        //3.判断有没有
        boolean b = filter.mightContain(1L);
        System.out.println("b = " + b);
        boolean b1 = filter.mightContain(21L);
        System.out.println("b1 = " + b1);
    }
}

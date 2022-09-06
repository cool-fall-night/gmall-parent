package com.atguigu.gmall.search;

import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.search.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 毛伟臣
 * 2022/9/6
 * 23:39
 * @version 1.0
 * @since JDK1.8
 */

@SpringBootTest
public class searchTest {

    @Autowired
    GoodsService goodsService;

    @Test
    public void testSearch(){

        SearchParam searchParam = new SearchParam();
        searchParam.setCategory3Id(61L);
        searchParam.setKeyword("小米");

        goodsService.search(searchParam);
    }
}

package com.atguigu.gmall.search.service;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;

/**
 * @author 毛伟臣
 * 2022/9/5
 * 23:56
 * @version 1.0
 * @since JDK1.8
 */

public interface GoodsService {


    /**
     * 在es中保存商品信息
     * @param goods
     */
    void savaGoods(Goods goods);

    void deleteGoods(Long skuId);

    SearchResponseVo search(SearchParam searchParam);

    void increaseHotScore(Long skuId, Long score);
}

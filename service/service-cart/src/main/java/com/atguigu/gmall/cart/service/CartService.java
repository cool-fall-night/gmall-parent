package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/9
 * 8:55
 * @version 1.0
 * @since JDK1.8
 */

public interface CartService {

    /**
     * 根据商品id添加至购物车
     * @param skuId
     * @param skuNum
     * @return
     */
    SkuInfo addToCart(Long skuId, Integer skuNum);


    /**
     * 根据userId查询cart列表
     * @param cartKey
     * @return
     */
    List<CartInfo> getCartList(String cartKey);

    void updateCartAllItemPrice(String cartKey, List<CartInfo> cartInfos);

    /**
     * 合并购物车
     */
    void mergeUserAndTempCart();

    /**
     * 获取redis中存储购物车的key值
     * @return
     */
    String determineCartKey();

    /**
     * 修改购物车内商品数量
     * @param skuId
     * @param num
     * @param cartKey
     */
    void updateItemNum(Long skuId, Integer num, String cartKey);

    /**
     * 修改商品选定状态
     * @param skuId
     * @param isChecked
     * @param cartKey
     */
    void updateCheckCart(Long skuId, Integer isChecked, String cartKey);

    /**
     * 删除购物车商品
     * @param skuId
     * @param cartKey
     */
    void deleteCart(Long skuId, String cartKey);
}

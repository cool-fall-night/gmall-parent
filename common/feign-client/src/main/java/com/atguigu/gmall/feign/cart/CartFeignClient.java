package com.atguigu.gmall.feign.cart;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 22:23
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-cart")
@RequestMapping("/api/inner/rpc/cart")
public interface CartFeignClient {

    /**
     * 远程调用Cart微服务中的添加购物车方法
     * @param skuId
     * @param skuNum
     * @return SkuInfo
     */
    @GetMapping("/addToCart")
    Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum);

    /**
     * 远程调用删除购物车中选择的商品
     * @return
     */
    @DeleteMapping("/deleteChecked")
    Result deleteChecked();


    /**
     * 获取购物车内被选择的商品详情
     * @return
     */
    @GetMapping("/getCheckedItem")
    Result<List<CartInfo>> getCheckedItem();

}

package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.annotation.EnableThreadPool;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/8
 * 18:39
 * @version 1.0
 * @since JDK1.8
 */

@EnableThreadPool
@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    @Autowired
    CartService cartService;

    @GetMapping("/cartList")
    public Result cartList(){

        String cartKey = cartService.determineCartKey();

        //合并购物车操作
        cartService.mergeUserAndTempCart();

        List<CartInfo> carts = cartService.getCartList(cartKey);

        return Result.ok(carts);
    }

    @PostMapping("/addToCart/{skuId}/{skuNum}")
    public Result updateItemNum(@PathVariable("skuId") Long skuId,
                                @PathVariable("skuNum") Integer num){

        String cartKey = cartService.determineCartKey();
        cartService.updateItemNum(skuId, num, cartKey);

        return Result.ok();

    }

    @GetMapping("/checkCart/{skuId}/{isChecked}")
    public Result checkCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("isChecked") Integer isChecked){

        String cartKey = cartService.determineCartKey();
        cartService.updateCheckCart(skuId, isChecked, cartKey);

        return Result.ok();
    }


    @DeleteMapping("/deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId){

        String cartKey = cartService.determineCartKey();
        cartService.deleteCart(skuId, cartKey);

        return Result.ok();
    }
}

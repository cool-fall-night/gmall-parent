package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum) {

        //添加购物车方法
        SkuInfo skuInfo = cartService.addToCart(skuId,skuNum);

        return Result.ok(skuInfo);
    }

    @DeleteMapping("/deleteChecked")
    public Result deleteChecked(){

        String cartKey = cartService.determineCartKey();
        cartService.deleteChecked(cartKey);

        return Result.ok();
    }

    @GetMapping("/getCheckedItem")
    public Result getCheckedItem(){

        String cartKey = cartService.determineCartKey();
        List<CartInfo> checkedItem = cartService.getCheckedItem(cartKey);

        return Result.ok(checkedItem);
    }


}

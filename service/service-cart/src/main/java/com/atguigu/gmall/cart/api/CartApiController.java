package com.atguigu.gmall.cart.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/9/8
 * 18:39
 * @version 1.0
 * @since JDK1.8
 */

@RestController
@RequestMapping("/api/inner/rpc/cart")
public class CartApiController {

    @GetMapping("/addToCart")
    public Result<SkuInfo> addToCart(@RequestParam("skuId") Long skuId,
                                     @RequestParam("skuNum") Integer skuNum) {

//        ,
//        @RequestHeader(value = "userId",required = false) String userId,
//        @RequestHeader(value = "userTempId",required = false) String userTempId
//        System.out.println("userId = " + userId);
//        System.out.println("userTempId = " + userTempId);

        return Result.ok();
    }


}

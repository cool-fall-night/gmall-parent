package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/8
 * 18:20
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class CartController {

    @Autowired
    private CartFeignClient cartFeignClient;


    @GetMapping("/addCart.html")
    public String addCart(@RequestParam("skuId") Long skuId,
                          @RequestParam("skuNum") Integer skuNum,
                          Model model){

        Result<SkuInfo> result = cartFeignClient.addToCart(skuId, skuNum);

        //远程调用Cart微服务讲购物车信息存入redis，返回页面所需的skuInfo和skuNum
        model.addAttribute("skuInfo",result.getData());
        model.addAttribute("skuNum",skuNum);

        return "cart/addCart";
    }

    @GetMapping("/cart.html")
    public String cartHtml(){

        return "cart/index";

    }

    @GetMapping("/cart/deleteChecked")
    public String deleteChecked(){

        cartFeignClient.deleteChecked();

        return "cart/index";
    }
}

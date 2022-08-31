package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.web.feign.SkuFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商品详情
 * @author 毛伟臣
 * 2022/8/26
 * 23:07
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class ItemController {

    @Autowired
    private SkuFeignClient skuFeignClient;

    @GetMapping("/{skuId}.html")
    public String item(@PathVariable Long skuId, Model model){

        //远程调用查询商品详情

        Result<SkuDetailTo> result = skuFeignClient.getSkuDetailTo(skuId);
        if (result.isOk()) {

            SkuDetailTo skuDetailTo = result.getData();

            if (skuDetailTo ==null || skuDetailTo.getSkuInfo() ==null){
                return "item/404";
            }

            model.addAttribute("categoryView",skuDetailTo.getCategoryView());
            model.addAttribute("skuInfo",skuDetailTo.getSkuInfo());
            model.addAttribute("price",skuDetailTo.getPrice());
            model.addAttribute("spuSaleAttrList",skuDetailTo.getSpuSaleAttrList());
            model.addAttribute("valuesSkuJson",skuDetailTo.getValuesSkuJson());
        }

        return "item/index";
    }
}
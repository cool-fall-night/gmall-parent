package com.atguigu.gmall.item.controller.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 23:50
 * @version 1.0
 * @since JDK1.8
 */

@RequestMapping("/api/inner/rpc/item")
@RestController
public class SkuDetailApiController {

    @Autowired
    private SkuDetailService skuDetailService;

    @GetMapping("/getSkuDetailTo/{skuId}")
    public Result<SkuDetailTo> getSkuDetailTo(@PathVariable Long skuId){

        SkuDetailTo skuDetailTo = skuDetailService.getSkuDetailTo(skuId);

        return Result.ok(skuDetailTo);
    }
}

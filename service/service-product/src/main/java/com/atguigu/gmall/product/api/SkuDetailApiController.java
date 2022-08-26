package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 分类有关的API
 * API均为远程调用的内部接口，命名规则：/api/inner/rpc/模块名/路径
 * @author 毛伟臣
 * 2022/8/26
 * 21:35
 * @version 1.0
 * @since JDK1.8
 *
 */

@Api(tags = "商品详情的API")
@RestController
@RequestMapping("/api/inner/rpc/product/")
public class SkuDetailApiController {

    @Autowired
    private SkuInfoService skuInfoService;


    @GetMapping("/getSkuDetailTo/{skuId}")
    Result<SkuDetailTo> getSkuDetailTo(@PathVariable Long skuId){

        SkuDetailTo skuDetailTo =  skuInfoService.getSkuDetailTo(skuId);

        return Result.ok(skuDetailTo);
    }
}

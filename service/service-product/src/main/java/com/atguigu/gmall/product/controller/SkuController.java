package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 0:31
 * @version 1.0
 * @since JDK1.8
 */

@Api(tags = "Sku模块")
@RestController
@RequestMapping("/admin/product")
public class SkuController {

    @Autowired
    private SkuInfoService skuInfoService;

    /**
     * 获取sku分页列表
     * GET请求
     * 请求头：/admin/product/list/{page}/{limit}
     */
    @ApiOperation(value = "获取sku分页列表")
    @GetMapping("/list/{num}/{size}")
    public Result list(@PathVariable("num") Long num,
                       @PathVariable("size") Long size){

        //分页插件
        Page<SkuInfo> page = new Page<>(num,size);
        //分页查询
        Page<SkuInfo> result = skuInfoService.page(page);

        return Result.ok(result);
    }

    /**
     * 添加sku
     * POST请求
     * 请求头：/admin/product/saveSkuInfo
     */
    @ApiOperation(value = "获取sku分页列表")
    @PostMapping("/saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){

        skuInfoService.saveSkuInfo(skuInfo);

        return Result.ok();
    }

    /**
     * sku上架
     * GET请求
     * 请求头：/admin/product/onSale/{skuId}
     */
    @ApiOperation(value = "sku上架")
    @GetMapping("/onSale/{skuId}")
    public Result onSale(@PathVariable("skuId") Long skuId){

        skuInfoService.onSale(skuId);
        return Result.ok();
    }

    /**
     * sku下架
     * GET请求
     * 请求头：/admin/product/cancelSale/{skuId}
     */
    @ApiOperation(value = "sku下架")
    @GetMapping("/cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId") Long skuId){

        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }

}

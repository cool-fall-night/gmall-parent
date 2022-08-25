package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.product.service.BaseSaleAttrService;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 23:01
 * @version 1.0
 * @since JDK1.8
 */
@Api(tags = "Spu模块")
@RestController
@RequestMapping("/admin/product")
public class SpuController {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private BaseSaleAttrService baseSaleAttrService;
    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;


    /**
     * 获取spu分页列表
     * GET请求
     * 请求头：/admin/product/{page}/{limit}?category3Id=61
     * return：JSON字符串封装对象
     * -@PathVariable:路径变量
     * -@RequestParam：请求参数（请求体中的某个数据）
     * -@RequestBody：请求参数（请求体中的全部数据）
     */
    @ApiOperation(value = "获取spu分页列表")
    @GetMapping("/{num}/{size}")
    public Result getSpuPage(@PathVariable("num") Long num,
                             @PathVariable("size") Long size,
                             @RequestParam("category3Id") Long category3Id){

        //分页插件
        Page<SpuInfo> page = new Page<>(num,size);
        //分页查询
        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);
        Page<SpuInfo> result = spuInfoService.page(page,wrapper);


        return Result.ok(result);

    }

    /**
     * 获取销售属性
     * GET请求
     * 请求头：/admin/product/baseSaleAttrList
     */
    @ApiOperation(value = "获取销售属性")
    @GetMapping("/baseSaleAttrList")
    public Result baseSaleAttrList(){

        List<BaseSaleAttr> list = baseSaleAttrService.list();

        return Result.ok(list);

    }

    /**
     * 添加spu
     * POST请求
     * 请求头：/admin/product/saveSpuInfo
     */
    @ApiOperation(value = "添加spu")
    @PostMapping("/saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){

        spuInfoService.saveSpuInfo(spuInfo);

        return Result.ok();

    }

    /**
     * 根据spuId获取图片列表
     * GET请求
     * 请求头：/admin/product/spuImageList/{spuId}
     */
    @ApiOperation(value = "根据spuId获取图片列表")
    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable Long spuId){

        QueryWrapper<SpuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("spu_id",spuId);
        List<SpuImage> list = spuImageService.list(wrapper);

        return Result.ok(list);
    }

    /**
     * 根据spuId获取销售属性
     * GET请求
     * 请求头：/admin/product/spuSaleAttrList/{spuId}
     */
    @ApiOperation(value = "根据spuId获取销售属性")
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result spuSaleAttrList(@PathVariable Long spuId){

        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrList(spuId);

        return Result.ok(list);
    }


}

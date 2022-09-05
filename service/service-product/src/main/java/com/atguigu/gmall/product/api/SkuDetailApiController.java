package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.model.to.ValueSkuJsonTo;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

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
@RequestMapping("/api/inner/rpc/product/skuDetailTo")
public class SkuDetailApiController {

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private BaseCategory3Service baseCategory3Service;


    /**
     * 查询商品基本信息
     * @param skuId
     * @return
     */
    @GetMapping("/info/{skuId}")
    @ApiOperation(value = "查询商品基本信息")
    public Result<SkuInfo> getSkuInfo(@PathVariable Long skuId){
        SkuInfo skuInfo = skuInfoService.getSkuInfo(skuId);
        return Result.ok(skuInfo);
    }

    /**
     * 查询商品图片信息
     * @param skuId
     * @return
     */
    @GetMapping("/images/{skuId}")
    @ApiOperation(value = "查询商品图片信息")
    public Result<List<SkuImage>> getSkuImages(@PathVariable Long skuId){
        List<SkuImage> skuImages = skuInfoService.getSkuImages(skuId);
        return Result.ok(skuImages);
    }

    /**
     * 查询商品实时价格
     * @param skuId
     * @return
     */
    @GetMapping("/price/{skuId}")
    @ApiOperation(value = "查询商品实时价格")
    public Result<BigDecimal> getSkuPrice(@PathVariable Long skuId){
        BigDecimal skuPrice = skuInfoService.getSkuPrice(skuId);
        return Result.ok(skuPrice);
    }

    /**
     * 查询商品属性名值
     * @param skuId
     * @return
     */
    @GetMapping("/spuSaleAttrList/{skuId}/{spuId}")
    @ApiOperation(value = "查询商品属性名值")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrValues(@PathVariable("skuId") Long skuId,
                                                          @PathVariable("spuId") Long spuId){

        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrMarkList(spuId,skuId);
        return Result.ok(list);
    }

    /**
     * 查询sku组合
     * @param spuId
     * @return
     */
    @GetMapping("/allSkuSaleAttrValueJson/{spuId}")
    @ApiOperation(value = "查询sku组合")
    public Result<String> getAllSkuSaleAttrValueJson(@PathVariable("spuId") Long spuId){

        String json = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);

        return Result.ok(json);
    }

    /**
     * 查询商品分类
     * @param category3Id
     * @return
     */
    @GetMapping("/categoryView/{category3Id}")
    @ApiOperation(value = "查询商品分类")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id") Long category3Id){

        CategoryViewTo categoryViewTo =baseCategory3Service.getCategoryView(category3Id);

        return Result.ok(categoryViewTo);
    }





}

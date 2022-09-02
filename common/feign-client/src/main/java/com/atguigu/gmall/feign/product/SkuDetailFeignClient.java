package com.atguigu.gmall.feign.product;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.model.to.CategoryViewTo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/27
 * 0:27
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface SkuDetailFeignClient {


    /**
     * 查询商品基本信息
     * @param skuId
     * @return
     */
    @GetMapping("/skuDetailTo/info/{skuId}")
    @ApiOperation(value = "查询商品基本信息")
    Result<SkuInfo> getSkuInfo(@PathVariable Long skuId);

    /**
     * 查询商品图片信息
     * @param skuId
     * @return
     */
    @GetMapping("/skuDetailTo/images/{skuId}")
    @ApiOperation(value = "查询商品图片信息")
    Result<List<SkuImage>> getSkuImages(@PathVariable Long skuId);

    /**
     * 查询商品实时价格
     * @param skuId
     * @return
     */
    @GetMapping("/skuDetailTo/price/{skuId}")
    @ApiOperation(value = "查询商品实时价格")
    Result<BigDecimal> getSkuPrice(@PathVariable Long skuId);

    /**
     * 查询商品属性名值
     * @param skuId
     * @param spuId
     * @return
     */
    @GetMapping("/skuDetailTo/spuSaleAttrList/{skuId}/{spuId}")
    @ApiOperation(value = "查询商品属性名值")
    Result<List<SpuSaleAttr>> getSpuSaleAttrValues(@PathVariable("skuId") Long skuId,
                                                   @PathVariable("spuId") Long spuId);


    /**
     * 查询sku组合
     * @param spuId
     * @return
     */
    @GetMapping("/skuDetailTo/allSkuSaleAttrValueJson/{spuId}")
    @ApiOperation(value = "查询sku组合")
    Result<String> getAllSkuSaleAttrValueJson(@PathVariable("spuId") Long spuId);

    /**
     * 查询商品分类
     * @param category3Id
     * @return
     */
    @GetMapping("/skuDetailTo/categoryView/{category3Id}")
    @ApiOperation(value = "查询商品分类")
    Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id") Long category3Id);

    /**
     * 远程调用getAllCategoryWithTree方法
     * 拿到相应json
     * @return
     */
    @GetMapping("/category/tree")
    Result<List<CategoryTreeTo>> getAllCategoryWithTree();

}

package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 毛伟臣
 * 2022/8/27
 * 0:14
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    private SkuDetailFeignClient skuDetailFeignClient;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public SkuDetailTo getSkuDetailTo(@PathVariable Long skuId){

        SkuDetailTo detailTo = new SkuDetailTo();

        //1、查基本信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            SkuInfo skuInfo = result.getData();
            return skuInfo;
        }, executor);


        //2、查商品图片信息
        CompletableFuture<Void> imagesFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            skuInfo.setSkuImageList(skuImages.getData());
            detailTo.setSkuInfo(skuInfo);
        }, executor);


        //3、查实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> skuPrice = skuDetailFeignClient.getSkuPrice(skuId);
            detailTo.setPrice(skuPrice.getData());
        }, executor);


        //4、查销售属性名值
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<List<SpuSaleAttr>> spuSaleAttrValues = skuDetailFeignClient.getSpuSaleAttrValues(skuId, skuInfo.getSpuId());
            detailTo.setSpuSaleAttrList(spuSaleAttrValues.getData());
        }, executor);


        //5、查sku组合
        CompletableFuture<Void> saleAttrValueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<String> allSkuSaleAttrValueJson = skuDetailFeignClient.getAllSkuSaleAttrValueJson(skuInfo.getSpuId());
            detailTo.setValuesSkuJson(allSkuSaleAttrValueJson.getData());
        }, executor);


        //6、查商品分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, executor);

        CompletableFuture.allOf(imagesFuture,priceFuture,saleAttrFuture,saleAttrValueJsonFuture,categoryViewFuture).join();

        return detailTo;

    }
}

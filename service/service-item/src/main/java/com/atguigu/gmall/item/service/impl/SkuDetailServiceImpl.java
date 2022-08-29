package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    StringRedisTemplate redisTemplate;

    public SkuDetailTo getSkuDetailFromRpc(@PathVariable Long skuId)  {

        SkuDetailTo detailTo = new SkuDetailTo();

        //1、查基本信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            return result.getData();
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


    @Override
    public SkuDetailTo getSkuDetailTo(Long skuId) {

        String jsonStr = redisTemplate.opsForValue().get("sku:info:" + skuId);

        if (StringUtils.isEmpty(jsonStr)){
            //redis中没有对应的值
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //回源:将对象转化为Json存入redis中(空值缓存)
            String cacheJson = "x";
            if (fromRpc != null){
                cacheJson = Jsons.toJsonStr(fromRpc);
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson,7, TimeUnit.DAYS);
            }else{
                redisTemplate.opsForValue().set("sku:info:" + skuId, cacheJson,30, TimeUnit.MINUTES);
            }
            return fromRpc;
        }
        //redis中有
        SkuDetailTo detailTo = Jsons.toObj(jsonStr,SkuDetailTo.class);

        return detailTo;
    }
}

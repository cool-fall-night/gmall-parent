package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.item.cache.CacheOpsService;
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

import static org.reflections.Reflections.log;

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

    @Autowired
    CacheOpsService cacheOpsService;

    public SkuDetailTo getSkuDetailFromRpc(@PathVariable Long skuId) {

        SkuDetailTo detailTo = new SkuDetailTo();

        //1、查基本信息
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            Result<SkuInfo> result = skuDetailFeignClient.getSkuInfo(skuId);
            return result.getData();
        }, executor);


        //2、查商品图片信息
        CompletableFuture<Void> imagesFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
                skuInfo.setSkuImageList(skuImages.getData());
                detailTo.setSkuInfo(skuInfo);
            }
        }, executor);


        //3、查实时价格
        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            Result<BigDecimal> skuPrice = skuDetailFeignClient.getSkuPrice(skuId);
            detailTo.setPrice(skuPrice.getData());
        }, executor);


        //4、查销售属性名值
        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                Result<List<SpuSaleAttr>> spuSaleAttrValues = skuDetailFeignClient.getSpuSaleAttrValues(skuId, skuInfo.getSpuId());
                detailTo.setSpuSaleAttrList(spuSaleAttrValues.getData());
            }
        }, executor);


        //5、查sku组合
        CompletableFuture<Void> saleAttrValueJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                Result<String> allSkuSaleAttrValueJson = skuDetailFeignClient.getAllSkuSaleAttrValueJson(skuInfo.getSpuId());
                detailTo.setValuesSkuJson(allSkuSaleAttrValueJson.getData());
            }
        }, executor);


        //6、查商品分类
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            if (skuInfo != null) {
                Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(skuInfo.getCategory3Id());
                detailTo.setCategoryView(categoryView.getData());
            }
        }, executor);

        CompletableFuture.allOf(imagesFuture, priceFuture, saleAttrFuture, saleAttrValueJsonFuture, categoryViewFuture).join();

        return detailTo;

    }

    @Deprecated
//    @Override
    public SkuDetailTo getSkuDetailToV1(Long skuId) {

        String jsonStr = redisTemplate.opsForValue().get(RedisConst.SKU_INFO_PREFIX + skuId);

        if (StringUtils.isEmpty(jsonStr)) {
            //redis中没有对应的值
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //回源:将对象转化为Json存入redis中(空值缓存)
            String cacheJson = "x";
            if (fromRpc != null) {
                cacheJson = Jsons.toJsonStr(fromRpc);
                redisTemplate.opsForValue().set(RedisConst.SKU_INFO_PREFIX + skuId, cacheJson, 7, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(RedisConst.SKU_INFO_PREFIX + skuId, cacheJson, 30, TimeUnit.MINUTES);
            }
            return fromRpc;
        }
        //redis中有
        SkuDetailTo detailTo = Jsons.toObj(jsonStr, SkuDetailTo.class);

        return detailTo;
    }

    @Override
    public SkuDetailTo getSkuDetailTo(Long skuId) {

        String cacheKey = RedisConst.SKU_INFO_PREFIX + skuId;
        //1、先查缓存
        SkuDetailTo cacheData = cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
        //2、判断缓存中是否存在
        if (cacheData == null) {
            //3、缓存中没有
            //4、询问布隆，该商品是否存在
            Boolean contain = cacheOpsService.bloomContains(skuId);
            if (!contain) {
                //5、布隆说没有，则确认没有该商品
                log.info("[{}]商品 - 布隆判定为无，检测到隐藏的攻击风险");
                return null;
            }
        }
        //6、布隆说有，则可能有该商品，回源查数据
        Boolean lock = cacheOpsService.tryLock(skuId);
        //为当前商品添加自己的分布式锁
        if (lock) {
            //7、获取锁成功，查询数据库
            log.info("[{}]商品 - 布隆判定为有，回源查数据");
            SkuDetailTo fromRpc = getSkuDetailFromRpc(skuId);
            //8、数据存入缓存
            cacheOpsService.saveData(cacheKey, fromRpc);
            //9、解锁
            cacheOpsService.unLock(skuId);
            return fromRpc;
        }
        //10、没获取到锁
        try {
            Thread.sleep(1000);
            return cacheOpsService.getCacheData(cacheKey, SkuDetailTo.class);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}


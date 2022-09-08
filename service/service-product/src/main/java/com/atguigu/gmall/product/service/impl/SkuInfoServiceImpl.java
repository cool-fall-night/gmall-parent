package com.atguigu.gmall.product.service.impl;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchAttr;

import java.util.Date;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-08-23 20:40:05
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageService skuImageService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private BaseTrademarkService baseTrademarkService;
    @Autowired
    private BaseCategory3Service baseCategory3Service;
    @Autowired
    private SearchFeignClient searchFeignClient;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {

        skuInfoMapper.insert(skuInfo);
        Long skuId = skuInfo.getId();

        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList){
            //spuId回填
            skuImage.setSkuId(skuId);
        }
        //批量保存图片
        skuImageService.saveBatch(skuImageList);

        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList){
            skuAttrValue.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(skuAttrValueList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue: skuSaleAttrValueList){
            skuSaleAttrValue.setSkuId(skuId);
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

        RBloomFilter<Object> filter = redissonClient.getBloomFilter(RedisConst.BLOOM_SKUID);

        filter.add(skuId);
    }

    @Override
    public SkuInfo getSkuInfo(Long skuId) {

        return skuInfoMapper.selectById(skuId);
    }

    @Override
    public List<SkuImage> getSkuImages(Long skuId) {

        return skuImageService.getSkuImages(skuId);
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return skuInfoMapper.get1010Price(skuId);
    }

    @Override
    public List<Long> findAllSkuId() {
        return skuInfoMapper.findAllSkuId();
    }

    @Override
    public void onSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,1);
        //TODO 在es中保存这个商品，可以被检索
        Goods goods = getGoodsBySkuId(skuId);
        searchFeignClient.saveGoods(goods);

    }

    @Override
    public void cancelSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,0);
        //TODO 在es中删除这个商品
        searchFeignClient.deleteGoods(skuId);
    }

    @Override
    public Goods getGoodsBySkuId(Long skuId) {

        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        Goods goods = new Goods();
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        goods.setTmId(skuInfo.getTmId());

        BaseTrademark trademark = baseTrademarkService.getById(skuInfo.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());
        CategoryViewTo categoryView = baseCategory3Service.getCategoryView(skuInfo.getCategory3Id());

        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());

        //TODO 热度分更新
        goods.setHotScore(0L);

        List<SearchAttr> searchAttrList = skuAttrValueService.getAttrNameAndValueBySkuId(skuId);
        goods.setAttrs(searchAttrList);

        return goods;
    }
}





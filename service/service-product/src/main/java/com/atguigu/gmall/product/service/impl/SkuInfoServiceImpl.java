package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.*;
import com.atguigu.starter.cache.constant.RedisConst;
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
    private BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private RedissonClient redissonClient;

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
    public void onSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,1);

    }

    @Override
    public void cancelSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,0);

    }

    @Deprecated
    @Override
    public SkuDetailTo getSkuDetailTo(Long skuId) {

        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //2、商品sku的基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        Long category3Id = skuInfo.getCategory3Id();
        Long spuId = skuInfo.getSpuId();
        //1、商品sku完整的分类信息
        CategoryViewTo categoryViewTo =baseCategory3Mapper.getCategoryView(category3Id);
        skuDetailTo.setCategoryView(categoryViewTo);
        //2.1、商品skuInfo中的skuImageList
        List<SkuImage> skuImageList = skuImageService.getSkuImages(skuId);
        skuInfo.setSkuImageList(skuImageList);
        //2.4、商品实时价格
        skuDetailTo.setPrice(this.getSkuPrice(skuId));
        //2.2、商品skuInfo中的skuAttrValueList（包含选定商品属性高亮显示）
        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrMarkList(spuId,skuId);
        skuDetailTo.setSpuSaleAttrList(list);
        //2.3、商品skuInfo中的skuSaleAttrValueList
        skuDetailTo.setSkuInfo(skuInfo);
        //3、商品的兄弟产品，查询全部销售属性名和值组合关系，并封装为{"118|120:50","销售A属性值|销售B属性值：skuId"}
        String json = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        skuDetailTo.setValuesSkuJson(json);

        return skuDetailTo;
    }




    @Override
    public SkuInfo getSkuInfo(Long skuId) {

        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        return skuInfo;
    }

    @Override
    public List<SkuImage> getSkuImages(Long skuId) {

        List<SkuImage> skuImageList = skuImageService.getSkuImages(skuId);

        return skuImageList;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return skuInfoMapper.get1010Price(skuId);
    }

    @Override
    public List<Long> findAllSkuId() {
        return skuInfoMapper.findAllSkuId();
    }


}





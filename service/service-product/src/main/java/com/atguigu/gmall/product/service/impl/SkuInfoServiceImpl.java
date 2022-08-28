package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    }

    @Override
    public void onSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,1);

    }

    @Override
    public void cancelSale(Long skuId) {

        skuInfoMapper.updateIsSale(skuId,0);

    }


    @Override
    public SkuDetailTo getSkuDetailTo(Long skuId) {

        SkuDetailTo skuDetailTo = new SkuDetailTo();
        //1、商品sku完整的分类信息
        CategoryViewTo categoryViewTo =baseCategory3Mapper.getCategoryView(skuId);
        skuDetailTo.setCategoryView(categoryViewTo);
        //2、商品sku的基本信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        //2.1、商品skuInfo中的skuImageList
        List<SkuImage> skuImageList = skuImageService.getSkuImages(skuId);
        skuInfo.setSkuImageList(skuImageList);
        //2.4、商品实时价格
        skuDetailTo.setPrice(this.get1010Price(skuId));
        //2.2、商品skuInfo中的skuAttrValueList
//        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrList(skuInfo.getSpuId());
        //TODO 高亮标记异常
        List<SpuSaleAttr> list = spuSaleAttrService.spuSaleAttrMarkList(skuInfo.getSpuId(),skuId);
        skuDetailTo.setSpuSaleAttrList(list);
        //2.3、商品skuInfo中的skuSaleAttrValueList

        skuDetailTo.setSkuInfo(skuInfo);

        return skuDetailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {
        return skuInfoMapper.get1010Price(skuId);
    }
}





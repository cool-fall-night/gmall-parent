package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-08-23 20:40:05
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private SpuImageService spuImageService;
    @Autowired
    private SpuSaleAttrService spuSaleAttrService;
    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;


    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {

        //保存Spu的基本消息
        spuInfoMapper.insert(spuInfo);
        Long spuId = spuInfo.getId();

        //保存Spu的图片信息到spu_image表
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList){
            //spuId回填
            spuImage.setSpuId(spuId);
        }
        //批量保存图片
        spuImageService.saveBatch(spuImageList);

        //保存Spu的销售属性到spu_sale_attr
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList){
            spuSaleAttr.setSpuId(spuId);
            //保存Spu的销售属性值到spu_sale_attr_value
            List<SpuSaleAttrValue> values = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : values){
                //回填商品id和销售属性名称
                value.setSpuId(spuId);
                value.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            }
            spuSaleAttrValueService.saveBatch(values);
        }
        spuSaleAttrService.saveBatch(spuSaleAttrList);


    }
}





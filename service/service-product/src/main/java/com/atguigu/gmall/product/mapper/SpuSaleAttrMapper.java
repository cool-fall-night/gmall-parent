package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
* @createDate 2022-08-23 20:40:05
* @Entity com.atguigu.gmall.product.domain.SpuSaleAttr
*/
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {


    List<SpuSaleAttr> spuSaleAttrList(Long spuId);

    List<SpuSaleAttr> spuSaleAttrMarkList(Long spuId, Long skuId);
}





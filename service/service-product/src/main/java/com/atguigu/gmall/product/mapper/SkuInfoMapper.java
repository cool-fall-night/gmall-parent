package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
* @createDate 2022-08-23 20:40:05
* @Entity com.atguigu.gmall.product.domain.SkuInfo
*/
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {

    void updateIsSale(@Param("skuId") Long skuId, @Param("sale") int sale);

    BigDecimal get1010Price(Long skuId);

    List<Long> findAllSkuId();

}





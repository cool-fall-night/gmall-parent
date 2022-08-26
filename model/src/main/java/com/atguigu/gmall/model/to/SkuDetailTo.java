package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 23:41
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class SkuDetailTo {

    private CategoryViewTo categoryView;

    private SkuInfo skuInfo;

    private BigDecimal price;

    private List<SpuSaleAttr> spuSaleAttrList;

    private String valuesSkuJson;
}

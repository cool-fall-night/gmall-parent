package com.atguigu.gmall.model.to;

import com.atguigu.gmall.model.product.SkuInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 毛伟臣
 * 2022/8/28
 * 17:48
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class ValueSkuJsonTo {

    private Long skuId;

    private String valueJson;

}

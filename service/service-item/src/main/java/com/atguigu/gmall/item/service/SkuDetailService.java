package com.atguigu.gmall.item.service;

import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author 毛伟臣
 * 2022/8/27
 * 0:13
 * @version 1.0
 * @since JDK1.8
 */

public interface SkuDetailService {

    SkuDetailTo getSkuDetailTo(@PathVariable Long skuId);

    void updateHotScore(Long skuId);
}

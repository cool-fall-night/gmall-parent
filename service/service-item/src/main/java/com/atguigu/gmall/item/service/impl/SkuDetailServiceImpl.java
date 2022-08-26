package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

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

    @Override
    public SkuDetailTo getSkuDetailTo(@PathVariable Long skuId){


        Result<SkuDetailTo> result = skuDetailFeignClient.getSkuDetailTo(skuId);
        if (result.isOk()) {
            SkuDetailTo skuDetailTo = result.getData();
            return skuDetailTo;
        }

        return null;

    }
}

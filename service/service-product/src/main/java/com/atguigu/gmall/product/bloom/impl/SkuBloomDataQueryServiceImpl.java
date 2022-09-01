package com.atguigu.gmall.product.bloom.impl;

import com.atguigu.gmall.product.bloom.BloomDataQueryService;
import com.atguigu.gmall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/1
 * 19:55
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class SkuBloomDataQueryServiceImpl implements BloomDataQueryService {

    @Autowired
    private SkuInfoService skuInfoService;

    @Override
    public List queryData(){

        return skuInfoService.findAllSkuId();
    }
}

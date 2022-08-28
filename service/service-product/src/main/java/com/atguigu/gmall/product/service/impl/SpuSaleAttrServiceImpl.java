package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.model.to.ValueSkuJsonTo;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import com.atguigu.gmall.product.mapper.SpuSaleAttrValueMapper;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
* @author 毛伟臣
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-08-23 20:40:05
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService {

    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(Long spuId) {

        return spuSaleAttrMapper.spuSaleAttrList(spuId);
    }

    @Override
    public List<SpuSaleAttr> spuSaleAttrMarkList(Long spuId, Long skuId) {

        return spuSaleAttrMapper.spuSaleAttrMarkList(spuId, skuId);
    }

    @Override
    public String getAllSkuSaleAttrValueJson(Long spuId) {

        List<ValueSkuJsonTo> list = spuSaleAttrMapper.getAllSkuSaleAttrValueJson(spuId);
        Map<String, Long> map = new HashMap<>();
        list.stream().forEach(valueSkuJson->{
            String valueJson = valueSkuJson.getValueJson();
            Long skuId = valueSkuJson.getSkuId();
            map.put(valueJson,skuId);
        });
        return Jsons.toJsonStr(map);
    }
}





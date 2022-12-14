package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import com.atguigu.gmall.product.service.SkuImageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【sku_image(库存单元图片表)】的数据库操作Service实现
* @createDate 2022-08-23 20:40:05
*/
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
    implements SkuImageService {

    @Autowired
    private SkuImageMapper skuImageMapper;

    @Override
    public List<SkuImage> getSkuImages(Long skuId) {

        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuId);

        List<SkuImage> skuImageList = skuImageMapper.selectList(wrapper);

        return skuImageList;
    }
}





package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-23 20:40:05
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(long category1Id, long category2Id, long category3Id);

    void saveAttrInfoAndValue(BaseAttrInfo info);
}

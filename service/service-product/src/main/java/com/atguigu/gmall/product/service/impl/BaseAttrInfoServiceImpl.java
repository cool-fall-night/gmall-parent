package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-08-23 20:40:05
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(long category1Id, long category2Id, long category3Id) {
        return baseAttrInfoMapper.getAttrInfoAndValueByCategoryId(category1Id,category2Id,category3Id);
    }

    @Override
    public void saveAttrInfoAndValue(BaseAttrInfo info) {

        if (info.getId() == null){
            addAttrInfoAndValue(info);
        }else{
            updateAttrInfoAndValue(info);
        }



    }

    private void addAttrInfoAndValue(BaseAttrInfo info) {
        baseAttrInfoMapper.insert(info);
        List<BaseAttrValue> attrValueList = info.getAttrValueList();
        for (BaseAttrValue baseAttrValue:attrValueList){
            baseAttrValue.setAttrId(info.getId());
            baseAttrValueMapper.insert(baseAttrValue);
        }
    }

    private void updateAttrInfoAndValue(BaseAttrInfo info) {
        baseAttrInfoMapper.updateById(info);
        List<BaseAttrValue> attrValueList = info.getAttrValueList();

        List<Long> vids = new ArrayList<>();
        for (BaseAttrValue baseAttrValue:attrValueList){
            Long id = baseAttrValue.getId();
            if (id != null){
                vids.add(id);
            }
        }

        if (vids.size()>0){
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id",info.getId());
            deleteWrapper.notIn("id",vids);
            baseAttrValueMapper.delete(deleteWrapper);
        }else{
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id",info.getId());
            baseAttrValueMapper.delete(deleteWrapper);
        }

        for (BaseAttrValue baseAttrValue:attrValueList){

            if (baseAttrValue.getId() != null){
                baseAttrValueMapper.updateById(baseAttrValue);
            }
            if (baseAttrValue.getId() == null){
                baseAttrValue.setAttrId(info.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }
}





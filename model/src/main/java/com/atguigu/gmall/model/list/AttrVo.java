package com.atguigu.gmall.model.list;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 平台属性相关对象
 * @author 毛伟臣
 */
@Data
public class AttrVo implements Serializable {

    // 平台属性Id
    private Long attrId;//1

    //属性名称
    private String attrName;//网络制式，分类

    //当前属性值的集合
    private List<String> attrValueList = new ArrayList<>();
}


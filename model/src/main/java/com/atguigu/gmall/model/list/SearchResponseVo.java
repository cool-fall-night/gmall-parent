package com.atguigu.gmall.model.list;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 总的数据
@Data
public class SearchResponseVo implements Serializable {

    //1、检索所需的所有参数
    private SearchParam searchParam;
    //2、品牌面包屑
    private String trademarkParam;
    //3、属性面包屑  集合：元素为prop对象，属性包含attrId、attrName、attrValue
    private List<SearchAttr> propsParamList = new ArrayList<>();
    //以上为面包屑

    //4、所有品牌  集合：元素为trademark对象，属性包含tmId、tmName、tmLogoUrl
    private List<TrademarkVo> trademarkList = new ArrayList<>();
    //5、所有属性  集合：元素为baseAttrInfo对象,属性包含attrId、attrName、List<String> attrValueList
    private List<AttrVo> attrsList = new ArrayList<>();
    //6、页面排序信息 对象：属性包含sort(asc/desc)、type(1/2)
    private OrderMapVo orderMap;
    //7、商品信息，集合：元素为goods 属性包含id、defaultImg、price、title、
    private List<Goods> goodsList = new ArrayList<>();
    //8、分页信息
    private Integer pageNo;

    private Integer totalPages;

    private Integer pageSize;//每页显示的内容
    //9、当前url信息
    private String urlParam;


}

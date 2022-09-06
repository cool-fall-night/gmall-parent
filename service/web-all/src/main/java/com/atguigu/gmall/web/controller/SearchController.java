package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 毛伟臣
 * 2022/9/6
 * 0:41
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class SearchController {

    @Autowired
    private SearchFeignClient searchFeignClient;

    @GetMapping("/list.html")
    public String search(SearchParam searchParam, Model model){

        Result<SearchResponseVo> search = searchFeignClient.search(searchParam);
        SearchResponseVo data = search.getData();
        //1、检索条件
        model.addAttribute("searchParam",data.getSearchParam());
        //2、品牌面包屑
        model.addAttribute("trademarkParam",data.getTrademarkParam());
        //3、属性面包屑  集合：元素为prop对象，属性包含attrId、attrName、attrValue
        model.addAttribute("propsParamList",data.getPropsParamList());
        //4、所有品牌  集合：元素为trademark对象，属性包含tmId、tmName、tmLogoUrl
        model.addAttribute("trademarkList",data.getTrademarkList());
        //5、所有属性  集合：元素为baseAttrInfo对象,属性包含attrId、attrName、List<String> attrValueList
        model.addAttribute("attrsList",data.getAttrsList());
        //6、页面排序信息 对象：属性包含sort(asc/desc)、type(1/2)
        model.addAttribute("orderMap",data.getOrderMap());
        //7、商品信息，集合：元素为goods 属性包含id、defaultImg、price、title、
        model.addAttribute("goodsList",data.getGoodsList());
        //8、分页信息
        model.addAttribute("pageNo",data.getPageNo());
        model.addAttribute("totalPages",data.getTotalPages());
        //9、当前url信息
        model.addAttribute("urlParam",data.getUrlParam());

        return "list/index";
    }



}

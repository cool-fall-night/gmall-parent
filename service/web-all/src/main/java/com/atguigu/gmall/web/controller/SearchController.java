package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.search.SearchFeignClient;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public String search(@RequestBody SearchParam searchParam, Model model){

        Result<SearchResponseVo> searchVo = searchFeignClient.search(searchParam);
        SearchResponseVo data = searchVo.getData();

        model.addAttribute("searchParam",null);
        model.addAttribute("trademarkParam",null);
        model.addAttribute("urlParam",null);
        model.addAttribute("propsParamList",null);
        model.addAttribute("trademarkList",null);
        model.addAttribute("attrsList",null);
        model.addAttribute("orderMap",null);
        model.addAttribute("goodsList",null);
        model.addAttribute("pageNo",null);
        model.addAttribute("totalPages",null);

        return "list/index";
    }



}

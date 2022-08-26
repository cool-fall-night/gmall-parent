package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.web.feign.CategoryFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 21:25
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class IndexController {

    @Autowired
    private CategoryFeignClient categoryFeignClient;

    @GetMapping({"/", "/index"})
    public String indexPage(Model model){

        //查询所有菜单 远程调用方法
        Result<List<CategoryTreeTo>> result = categoryFeignClient.getAllCategoryWithTree();

        if (result.isOk()){
            List<CategoryTreeTo> data = result.getData();
            model.addAttribute("list",data);
        }

        return "index/index";
    }
}

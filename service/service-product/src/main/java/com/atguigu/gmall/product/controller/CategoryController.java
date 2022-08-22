package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/22
 * 22:01
 * @version 1.0
 * @since JDK1.8
 */

@RestController
public class CategoryController {

    @Autowired
    private BaseCategory1Service baseCategory1Service;
    @Autowired
    private BaseCategory2Service baseCategory2Service;
    @Autowired
    private BaseCategory3Service baseCategory3Service;

    /**
     * 查询所有的一级分类
     * GET请求
     * 请求头：/admin/product/getCategory1
     * @return：JSON字符串封装对象
     */
    @GetMapping("/admin/product/getCategory1")
    public Result getCategory1(){

        List<BaseCategory1> list = baseCategory1Service.list();
        return Result.ok(list);

    }

    /**
     * 根据一级分类ID查询所有的二级分类
     * GET请求
     * 请求头：/admin/product/getCategory2/{category1Id}
     * @return：JSON字符串封装对象
     */
    @GetMapping("/admin/product/getCategory2/{category1Id}")
    public Result getCategory2(@PathVariable long category1Id){

        List<BaseCategory2> category2s = baseCategory2Service.getCategory1Child(category1Id);
        return Result.ok(category2s);

    }

    /**
     * 根据二级分类ID查询所有的三级分类
     * GET请求
     * 请求头：/admin/product/getCategory3/{category2Id}
     * @return：JSON字符串封装对象
     */
    @GetMapping("/admin/product/getCategory3/{category2Id}")
    public Result getCategory3(@PathVariable long category2Id){

        List<BaseCategory3> category3s = baseCategory3Service.getCategory2Child(category2Id);
        return Result.ok(category3s);

    }
}

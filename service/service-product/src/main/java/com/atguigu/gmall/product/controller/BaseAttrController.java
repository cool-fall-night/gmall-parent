package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/23
 * 20:26
 * @version 1.0
 * @since JDK1.8
 *
 * @title 平台属性相关接口
 */

@RestController
@RequestMapping("/admin/product")
public class BaseAttrController {

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;
    @Autowired
    private BaseAttrValueService baseAttrValueService;

    /**
     * 根据分类id获取平台属性
     * GET请求
     * 请求：/admin/product/attrInfoList/{category1Id}/{category2Id}/{category3Id}
     * return：JSON字符串封装对象
     *
     * 说明：1.平台属性可以挂在一级分类、二级分类和三级分类；
     *      2.查询一级分类下面的平台属性，传：category1Id，0，0；取出该分类的平台属性；
     *      3.查询二级分类下面的平台属性，传：category1Id，category2Id，0；取出对应一级分类下面的平台属性与二级分类对应的平台属性；
     *      4.查询三级分类下面的平台属性，传：category1Id，category2Id，category3Id；取出对应一级分类、二级分类与三级分类对应的平台属性
     */
    @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
    public Result getAttrInfoList(@PathVariable("category1Id") long category1Id,
                               @PathVariable("category2Id") long category2Id,
                               @PathVariable("category3Id") long category3Id){

        List<BaseAttrInfo> infos =  baseAttrInfoService.getAttrInfoAndValueByCategoryId(category1Id,category2Id,category3Id);
        return Result.ok(infos);

    }

    /**
     * 添加/修改平台属性
     * POST请求
     * 请求：/admin/product/saveAttrInfo
     * return：JSON字符串封装对象
     */
    @PostMapping("/saveAttrInfo")
    public Result saveAttrInfo(@RequestBody BaseAttrInfo info){

        baseAttrInfoService.saveAttrInfoAndValue(info);
        //添加/修改平台属性
        return Result.ok();

    }

    /**
     * 根据平台属性ID获取平台属性对象数据
     * GET请求
     * 请求：/admin/product/getAttrValueList/{attrId}
     * return：JSON字符串封装对象
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId") long attrId){

        List<BaseAttrValue> values =  baseAttrValueService.getAttrValueList(attrId);

        return Result.ok(values);

    }
}



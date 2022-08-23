package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/8/24
 * 0:07
 * @version 1.0
 * @since JDK1.8
 */

@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    private BaseTrademarkService baseTrademarkService;


    /**
     * 获取品牌分页列表
     * GET请求
     * 请求：/admin/product/baseTrademark/{page}/{limit}
     * return：JSON字符串封装对象
     */
    @GetMapping("/{page}/{limit}")
    public Result baseTrademark(@PathVariable("page") Long num,
                                @PathVariable("limit") Long size){

        //分页插件
        Page<BaseTrademark> page = new Page<>(num,size);
        //分页查询
        Page<BaseTrademark> pageResult = baseTrademarkService.page(page);
        return Result.ok(pageResult);

    }

    /**
     * 添加品牌
     * POST请求
     * 请求：/admin/product/baseTrademark/save
     * return：JSON字符串封装对象
     */
    @PostMapping("/save")
    public Result save(@RequestBody BaseTrademark baseTrademark){

        baseTrademarkService.save(baseTrademark);
        return Result.ok();

    }

    /**
     * 修改品牌
     * PUT请求
     * 请求：/admin/product/baseTrademark/update
     * return：JSON字符串封装对象
     */
    @PutMapping("/update")
    public Result update(@RequestBody BaseTrademark baseTrademark){

        baseTrademarkService.updateById(baseTrademark);
        return Result.ok();

    }

    /**
     * 删除品牌
     * DELETE请求
     * 请求：/admin/product/baseTrademark/remove/{id}
     * return：JSON字符串封装对象
     */
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable("id") Long id){

        baseTrademarkService.removeById(id);
        return Result.ok();

    }

    /**
     * 根据Id获取品牌
     * GET请求
     * 请求：/admin/product/baseTrademark/get/{id}
     * return：JSON字符串封装对象
     */
    @GetMapping("/get/{id}")
    public Result get(@PathVariable("id") Long id){

        BaseTrademark baseTrademark = baseTrademarkService.getById(id);
        return Result.ok(baseTrademark);

    }
}


package com.atguigu.gmall.product.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类有关的API
 * API均为远程调用的内部接口，命名规则：/api/inner/rpc/模块名/路径
 * @author 毛伟臣
 * 2022/8/26
 * 21:35
 * @version 1.0
 * @since JDK1.8
 *
 */

@Api(tags = "分类有关的API")
@RestController
@RequestMapping("/api/inner/rpc/product/")
public class CategoryApiController {

    @Autowired
    private BaseCategory2Service baseCategory2Service;

    /**
     * 查询所有菜单并将结果封装为tree
     */
    @ApiOperation(value = "查询所有菜单并将结果封装为tree")
    @GetMapping("/category/tree")
    public Result getAllCategoryWithTree(){

        List<CategoryTreeTo> categoryTreeTos =  baseCategory2Service.getAllCategoryWithTree();
        return Result.ok(categoryTreeTos);
    }
}

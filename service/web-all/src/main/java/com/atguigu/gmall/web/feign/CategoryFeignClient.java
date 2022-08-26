package com.atguigu.gmall.web.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 22:23
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-product")
public interface CategoryFeignClient {

    /**
     * 远程调用getAllCategoryWithTree方法
     * 拿到相应json
     * @return
     */
    @GetMapping("/api/inner/rpc/category/tree")
    Result<List<CategoryTreeTo>> getAllCategoryWithTree();
}

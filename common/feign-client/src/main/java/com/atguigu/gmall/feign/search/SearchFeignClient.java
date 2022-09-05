package com.atguigu.gmall.feign.search;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/9/5
 * 23:50
 * @version 1.0
 * @since JDK1.8
 */
@FeignClient("service-search")
@RequestMapping("/api/inner/rpc/search")
public interface SearchFeignClient {

    @PostMapping("/saveGoods")
    Result<Goods> saveGoods(@RequestBody Goods goods);

    @DeleteMapping("/deleteGoods/{skuId}")
    Result<Goods> deleteGoods(@PathVariable Long skuId);


    @PostMapping("/goods/search")
    Result<SearchResponseVo> search(@RequestBody SearchParam searchParam);
}

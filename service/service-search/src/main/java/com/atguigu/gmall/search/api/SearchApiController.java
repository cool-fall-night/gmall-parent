package com.atguigu.gmall.search.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.list.SearchParam;
import com.atguigu.gmall.model.list.SearchResponseVo;
import com.atguigu.gmall.search.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/9/5
 * 23:40
 * @version 1.0
 * @since JDK1.8
 */

@Api(tags = "商品检索的API")
@RestController
@RequestMapping("/api/inner/rpc/search")
public class SearchApiController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/saveGoods")
    public Result<Goods> saveGoods(@RequestBody Goods goods){

        goodsService.savaGoods(goods);
        return Result.ok();
    }

    @DeleteMapping("/deleteGoods/{skuId}")
    public Result<Goods> deleteGoods(@PathVariable Long skuId){

        goodsService.deleteGoods(skuId);
        return Result.ok();
    }

    @PostMapping("/goods/search")
    public Result<SearchResponseVo> search(@RequestBody SearchParam searchParam){

        //TODO 商品检索
        //goodsService.search(searchParam);
        return Result.ok();
    }

}

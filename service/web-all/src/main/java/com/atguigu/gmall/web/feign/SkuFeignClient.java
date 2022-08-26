package com.atguigu.gmall.web.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 22:23
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-item")
@RequestMapping("/api/inner/rpc/item")
public interface SkuFeignClient {

    @GetMapping("/getSkuDetailTo/{skuId}")
    Result<SkuDetailTo> getSkuDetailTo(@PathVariable Long skuId);


}

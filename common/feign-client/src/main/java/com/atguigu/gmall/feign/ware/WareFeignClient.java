package com.atguigu.gmall.feign.ware;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 毛伟臣
 * 2022/9/14
 * 0:08
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient(value = "ware-manage",url = "${app.wareUrl:http://localhost:9001/}")
public interface WareFeignClient {

    /**
     * 查询商品是否有库存1有0没有
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/hasStock")
    String hasStock(@RequestParam("skuId") Long skuId,
                    @RequestParam("num") Integer num );


}

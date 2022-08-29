package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 毛伟臣
 * 2022/8/29
 * 23:39
 * @version 1.0
 * @since JDK1.8
 */

@RestController
public class SkuIdBitMapController {

    @GetMapping("/sync/skuid/bitmap")
    public Result syncBitMap() {
        return Result.ok();
    }
}

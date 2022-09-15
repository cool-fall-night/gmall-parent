package com.atguigu.gmall.order.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 毛伟臣
 * 2022/9/14
 * 9:16
 * @version 1.0
 * @since JDK1.8
 */

@Slf4j
@RestController
@RequestMapping("/api/order/auth")
public class OrderRestController {

    @Autowired
    OrderBizService orderBizService;

    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestParam("tradeNo") String tradeNo,
                              @RequestBody OrderSubmitVo submitVo){

        Long orderId = orderBizService.submitOrder(submitVo,tradeNo);

        return Result.ok(orderId.toString());
    }
}



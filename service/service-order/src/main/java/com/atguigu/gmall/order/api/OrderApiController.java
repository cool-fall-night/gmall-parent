package com.atguigu.gmall.order.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:49
 * @version 1.0
 * @since JDK1.8
 */

@RequestMapping("/api/inner/rpc/order")
@RestController
public class OrderApiController {

    @Autowired
    OrderBizService orderBizService;

    @GetMapping("/getOrderConfirm/data")
    public Result<OrderConfirmVo> getOrderConfirmData(){

        OrderConfirmVo data = orderBizService.getOrderConfirmData();

        return Result.ok(data);
    }

    @GetMapping("/getOrderInfo")
    Result<OrderInfo> getOrderInfo(String orderId){

        OrderInfo orderInfo = orderBizService.getOrderInfo(orderId);

        return Result.ok(orderInfo);
    }

}

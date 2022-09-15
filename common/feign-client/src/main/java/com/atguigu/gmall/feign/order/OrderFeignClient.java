package com.atguigu.gmall.feign.order;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 毛伟臣
 * 2022/8/27
 * 0:27
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-order")
@RequestMapping("/api/inner/rpc/order")
public interface OrderFeignClient {

    /**
     * 获取订单也的信息封装对象
     * @return
     */
    @GetMapping("/getOrderConfirm/data")
    Result<OrderConfirmVo> getOrderConfirmData();

    /**
     * 获取订单封装信息
     * @return
     * @param orderId
     */
    @GetMapping("/getOrderInfo")
    Result<OrderInfo> getOrderInfo(@RequestParam("orderId") String orderId);

}

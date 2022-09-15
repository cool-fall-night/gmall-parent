package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 14:02
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class PaymentController {

    @Autowired
    OrderFeignClient orderFeignClient;

    @GetMapping("pay.html")
    public String payPage(@RequestParam("orderId") String orderId, Model model){

        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();

        Date expireTime = orderInfo.getExpireTime();
        Date currentTime = new Date();
        if (expireTime.before(currentTime)){
            return "payment/overdue";
        }

        model.addAttribute("orderInfo",orderInfo);

        return "payment/pay";
    }
}
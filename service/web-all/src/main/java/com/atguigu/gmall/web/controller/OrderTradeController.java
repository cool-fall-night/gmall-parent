package com.atguigu.gmall.web.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 9:23
 * @version 1.0
 * @since JDK1.8
 */

@Controller
public class OrderTradeController {

    @Autowired
    OrderFeignClient orderFeignClient;


    @GetMapping("/trade.html")
    public String tradePage(Model model){

        Result<OrderConfirmVo> orderConfirmData = orderFeignClient.getOrderConfirmData();

        if (orderConfirmData.isOk()){
            OrderConfirmVo data = orderConfirmData.getData();
            //交易流水号
            model.addAttribute("tradeNo",data.getTradeNo());
            //总商品数
            model.addAttribute("totalNum",data.getTotalNum());
            //总商品价格
            model.addAttribute("totalAmount",data.getTotalAmount());
            //收货地址列表
            model.addAttribute("userAddressList",data.getUserAddressList());
            //商品详情列表
            model.addAttribute("detailArrayList",data.getDetailArrayList());
        }

        return "order/trade";
    }
}

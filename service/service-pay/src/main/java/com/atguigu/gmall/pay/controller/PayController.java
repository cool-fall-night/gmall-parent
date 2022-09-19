package com.atguigu.gmall.pay.controller;

import com.alipay.api.AlipayApiException;
import com.atguigu.gmall.pay.AlipayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author 毛伟臣
 * 2022/9/16
 * 19:40
 * @version 1.0
 * @since JDK1.8
 */

@Slf4j
@RequestMapping("/api/payment")
@Controller
public class PayController {

    @Autowired
    AlipayService alipayService;

    @ResponseBody
    @GetMapping("/alipay/submit/{orderId}")
    public String alipayPage(@PathVariable Long orderId) throws AlipayApiException {

        return alipayService.getAlipayPageHtml(orderId);
    }

    /**
     * 支付成功，同步通知
     * @param paramsMap
     * @return
     * @throws AlipayApiException
     */
    @GetMapping("/success/pay")
    public String paySuccessPage(@RequestParam Map<String,String> paramsMap) throws AlipayApiException {

        //修改订单状态前必须验单
        if (alipayService.rsaCheckV1(paramsMap)) {
            //验签通过
            log.info("验签通过，支付成功，同步通知抵达。订单信息： " + paramsMap);
        }


        return "redirect:http://gmall.com/pay/success.html";
    }

    /**
     * 支付成功，异步通知
     * @param paramsMap
     * @return
     * @throws AlipayApiException
     */
    @ResponseBody
    @RequestMapping("/success/notify")
    public String notifySuccess(@RequestParam Map<String,String> paramsMap) throws AlipayApiException {

        //修改订单状态前必须验单
        if (alipayService.rsaCheckV1(paramsMap)) {
            //验签通过
            log.info("验签通过，支付成功，异步通知抵达。订单信息： " + paramsMap);
            //TODO 修改订单状态
        }else {
            return "error";
        }

        return "success";
    }

}

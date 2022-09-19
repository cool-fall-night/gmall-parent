package com.atguigu.gmall.pay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.feign.order.OrderFeignClient;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.pay.AlipayService;
import com.atguigu.gmall.pay.config.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 毛伟臣
 * 2022/9/16
 * 21:14
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    AlipayClient alipayClient;
    @Autowired
    AlipayProperties alipayProperties;
    @Autowired
    OrderFeignClient orderFeignClient;

    /**
     * 生成指定订单的支付二维码页
     * @param orderId
     * @return
     */
    @Override
    public String getAlipayPageHtml(Long orderId) throws AlipayApiException {

        //1.创建一个支付请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //2.1 浏览器跳转到ReturnUrl
        //2.2 支付宝给NotifyUrl 发请求通知支付成功消息
        alipayRequest.setReturnUrl(alipayProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(alipayProperties.getNotifyUrl());

        //3.获取订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId.toString()).getData();
        if (orderInfo.getExpireTime().before(new Date())) {
            throw new GmallException(ResultCodeEnum.ORDER_OVERTIME);
        }

        //4.构造支付数据
        HashMap<String, Object> bizContent = new HashMap<>();
        bizContent.put("out_trade_no",orderInfo.getOutTradeNo());
        bizContent.put("total_amount",orderInfo.getTotalAmount());
        bizContent.put("subject","尚品汇订单-" + orderInfo.getOutTradeNo());
        bizContent.put("product_code","FAST_INSTANT_TRADE_PAY");
        bizContent.put("body",orderInfo.getTradeBody());

        //4.1 自动收单
        String date = DateUtil.formatDate(orderInfo.getExpireTime(), "yyyy-MM-dd HH:mm:ss");
        bizContent.put("time_expire",date);
        alipayRequest.setBizContent(Jsons.toJsonStr(bizContent));

        //5.用支付宝客户端发送请求，返回收银台页面
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

    @Override
    public boolean rsaCheckV1(Map<String, String> paramMaps) throws AlipayApiException {

        //修改订单状态前必须验签，确定返回数据的真实性
        return AlipaySignature.rsaCheckV1(paramMaps,
                alipayProperties.getAlipayPublicKey(),
                alipayProperties.getCharset(),
                alipayProperties.getSignType());
    }
}

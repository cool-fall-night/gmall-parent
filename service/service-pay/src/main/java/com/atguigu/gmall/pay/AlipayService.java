package com.atguigu.gmall.pay;

import com.alipay.api.AlipayApiException;

import java.util.Map;

/**
 * @author 毛伟臣
 * 2022/9/16
 * 21:12
 * @version 1.0
 * @since JDK1.8
 */

public interface AlipayService {
    String getAlipayPageHtml(Long orderId) throws AlipayApiException;

    boolean rsaCheckV1(Map<String, String> paramsMap) throws AlipayApiException;

}

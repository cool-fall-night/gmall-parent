package com.atguigu.gmall.order.biz;

import com.atguigu.gmall.model.vo.order.OrderConfirmVo;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:57
 * @version 1.0
 * @since JDK1.8
 */

public interface OrderBizService {

    OrderConfirmVo getOrderConfirmData();

    String getGenerateTradeNo();

    boolean checkGenerateTradeNo(String tradeNo);
}

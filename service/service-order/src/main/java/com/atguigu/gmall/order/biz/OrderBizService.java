package com.atguigu.gmall.order.biz;

import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:57
 * @version 1.0
 * @since JDK1.8
 */

public interface OrderBizService {

    /**
     * 获取订单确认数据
     * @return
     */
    OrderConfirmVo getOrderConfirmData();

    /**
     * 获取交易流水号
     * @return
     */
    String getGenerateTradeNo();

    /**
     * 检查交易流水号
     * @param tradeNo
     * @return
     */
    boolean checkGenerateTradeNo(String tradeNo);

    /**
     * 提交订单
     * @param submitVo
     * @param tradeNo
     * @return
     */
    Long submitOrder(OrderSubmitVo submitVo, String tradeNo);
}

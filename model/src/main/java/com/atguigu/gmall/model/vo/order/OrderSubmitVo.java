package com.atguigu.gmall.model.vo.order;

import com.atguigu.gmall.model.order.OrderDetail;
import lombok.Data;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/14
 * 9:54
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class OrderSubmitVo {

    //收货人
    String consignee;
    //收货人电话
    String consigneeTel;
    //收货地址
    String deliveryAddress;
    //订单留言
    String orderComment;
    //商品详情列表
    List<OrderDetail> orderDetailList;
    //支付方式
    String paymentWay;

}

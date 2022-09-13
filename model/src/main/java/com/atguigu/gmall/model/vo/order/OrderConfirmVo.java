package com.atguigu.gmall.model.vo.order;

import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.user.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:41
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class OrderConfirmVo {

    private String tradeNo;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private List<OrderDetail> detailArrayList;
    private List<UserAddress> userAddressList;

}

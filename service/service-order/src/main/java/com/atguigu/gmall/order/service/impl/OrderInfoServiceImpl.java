package com.atguigu.gmall.order.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.enums.OrderStatus;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.mapper.OrderInfoMapper;
import com.atguigu.gmall.order.service.OrderDetailService;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* @author 毛伟臣
* @description 针对表【order_info(订单表 订单表)】的数据库操作Service实现
* @createDate 2022-09-12 21:15:48
*/
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
    implements OrderInfoService {

    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderDetailService orderDetailService;


    @Override
    @Transactional
    public Long saveOrder(OrderSubmitVo submitVo, String tradeNo) {

        OrderInfo orderInfo = prepareOrderInfo(submitVo,tradeNo);

        orderInfoMapper.insert(orderInfo);

        //订单商品详情
        List<OrderDetail> orderDetailList = prepareOrderDetailList(submitVo,orderInfo);

        orderDetailService.saveBatch(orderDetailList);

        return orderInfo.getId();
    }

    private List<OrderDetail> prepareOrderDetailList(OrderSubmitVo submitVo, OrderInfo orderInfo) {

        List<OrderDetail> orderDetailList = submitVo.getOrderDetailList();
        orderDetailList.forEach(orderDetail -> {
            orderDetail.setOrderId(orderInfo.getId());
            orderDetail.setCreateTime(new Date());
            orderDetail.setUserId(orderInfo.getUserId());
        });

        return orderDetailList;
    }

    private OrderInfo prepareOrderInfo(OrderSubmitVo submitVo, String tradeNo) {

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setConsignee(submitVo.getConsignee());
        orderInfo.setConsigneeTel(submitVo.getConsigneeTel());
        orderInfo.setPaymentWay(submitVo.getPaymentWay());
        orderInfo.setDeliveryAddress(submitVo.getDeliveryAddress());
        orderInfo.setOrderComment(submitVo.getOrderComment());

        //订单总价
        BigDecimal totalAmount = submitVo.getOrderDetailList().stream().map(orderDetail ->
                orderDetail.getOrderPrice().multiply(new BigDecimal(orderDetail.getSkuNum() + ""))
        ).reduce(BigDecimal::add).get();
        orderInfo.setTotalAmount(totalAmount);

        //订单状态
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());

        //订单处理状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());

        //用户id（分片键）
        Long userId = AuthContextHolder.getCurrentAuthInfo().getUserId();
        orderInfo.setUserId(userId);

        //交易流水号
        orderInfo.setOutTradeNo(tradeNo);

        //交易缩略名（订单第一个商品名）
        String firstSkuName = submitVo.getOrderDetailList().get(0).getSkuName() + "等。。。";
        orderInfo.setTradeBody(firstSkuName);

        orderInfo.setCreateTime(new Date());

        //过期时间（30min）
        orderInfo.setExpireTime(new Date(System.currentTimeMillis() + RedisConst.EXPIRE_TIME));

        //物流单号
        orderInfo.setTrackingNo("");

        //父订单号（拆单）
        orderInfo.setParentOrderId(0L);

        String imgUrl = submitVo.getOrderDetailList().get(0).getImgUrl();
        orderInfo.setImgUrl(imgUrl);


        orderInfo.setActivityReduceAmount(new BigDecimal("0"));
        orderInfo.setCouponAmount(new BigDecimal("0"));
        orderInfo.setOriginalTotalAmount(totalAmount);
        //允许退款时间
        orderInfo.setRefundableTime(new Date(System.currentTimeMillis() + RedisConst.REFUNDABLE_TIME));
        // 运费
        orderInfo.setFeightFee(new BigDecimal("0"));
        orderInfo.setOperateTime(new Date());

        return orderInfo;
    }
}





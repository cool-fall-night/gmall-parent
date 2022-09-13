package com.atguigu.gmall.order.biz.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuDetailFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:58
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class OrderBizServiceImpl implements OrderBizService {

    @Autowired
    CartFeignClient cartFeignClient;
    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;
    @Autowired
    UserFeignClient userFeignClient;
    @Autowired
    WareFeignClient wareFeignClient;

    @Override
    public OrderConfirmVo getOrderConfirmData() {

        //TODO 获取订单页数据封装对象
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

        //交易流水号
        orderConfirmVo.setTradeNo(UUID.randomUUID().toString().replace("-",""));

        //商品详情列表
        List<CartInfo> cartInfos = cartFeignClient.getCheckedItem().getData();
        /*List<OrderDetail> details = new ArrayList<>();
        for (CartInfo cartInfo : cartInfos){
            details.add(makeOrderDetailByCartInfo(cartInfo));
        }*/
        List<OrderDetail> details = cartInfos.stream().map(this::makeOrderDetailByCartInfo).collect(Collectors.toList());

        orderConfirmVo.setDetailArrayList(details);

        //总商品数
        Integer totalNum = details.stream().map(OrderDetail::getSkuNum)
                .reduce(Integer::sum).get();
        orderConfirmVo.setTotalNum(totalNum);

        //总商品价格
        BigDecimal totalAmount = details.stream().map(OrderDetail::getTotalOrderPrice)
                .reduce(BigDecimal::add).get();
        orderConfirmVo.setTotalAmount(totalAmount);

        //收货地址列表
        List<UserAddress> userAddresses = userFeignClient.getUserAddressList().getData();
        orderConfirmVo.setUserAddressList(userAddresses);

        return orderConfirmVo;
    }

    private OrderDetail makeOrderDetailByCartInfo(CartInfo cartInfo) {

        BigDecimal realPrice = skuDetailFeignClient.getSkuPrice(cartInfo.getSkuId()).getData();

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setSkuId(cartInfo.getSkuId());
        orderDetail.setSkuName(cartInfo.getSkuName());
        orderDetail.setImgUrl(cartInfo.getImgUrl());
        orderDetail.setSkuNum(cartInfo.getSkuNum());
        orderDetail.setOrderPrice(realPrice);
        orderDetail.setTotalOrderPrice(realPrice.multiply(new BigDecimal(cartInfo.getSkuNum())));
        String hasStock = wareFeignClient.hasStock(cartInfo.getSkuId(), cartInfo.getSkuNum());
        orderDetail.setHasStock(hasStock);
        return orderDetail;
    }
}

package com.atguigu.gmall.order.biz.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.constant.MqConst;
import com.atguigu.gmall.feign.cart.CartFeignClient;
import com.atguigu.gmall.feign.product.SkuDetailFeignClient;
import com.atguigu.gmall.feign.user.UserFeignClient;
import com.atguigu.gmall.feign.ware.WareFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.order.OrderInfo;
import com.atguigu.gmall.model.to.mq.OrderMsg;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.model.vo.order.OrderConfirmVo;
import com.atguigu.gmall.model.vo.order.OrderSubmitVo;
import com.atguigu.gmall.order.biz.OrderBizService;
import com.atguigu.gmall.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 19:58
 * @version 1.0
 * @since JDK1.8
 */
@Slf4j
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
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    OrderInfoService orderInfoService;

    @Override
    public OrderConfirmVo getOrderConfirmData() {

        //获取订单页数据封装对象
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();

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

        //交易流水号
        String tradeNo = getGenerateTradeNo();
        orderConfirmVo.setTradeNo(tradeNo);


        return orderConfirmVo;
    }

    /**
     * 生成交易流水号
     * @return
     */
    @Override
    public String getGenerateTradeNo() {

        //当前毫秒数
        Long millis = System.currentTimeMillis();

        Long userId = AuthContextHolder.getCurrentAuthInfo().getUserId();

        String tradeNo = millis + "_" + userId;

        redisTemplate.opsForValue().set(RedisConst.ORDER_TEMP_TOKEN + tradeNo,"1",15, TimeUnit.MINUTES);

        return tradeNo;
    }

    /**
     * 校验交易流水号
     * @return
     */
    @Override
    public boolean checkGenerateTradeNo(String tradeNo){

        String lua1 = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                     "then return redis.call(\"del\",KEYS[1]) " +
                     "else return 0 " +
                     "end";

        //1、先看有没有，如果有就是正确令牌, 1, 0 。脚本校验令牌
        String lua = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end";

        Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(lua,Long.class),
                Arrays.asList(RedisConst.ORDER_TEMP_TOKEN + tradeNo), new String[]{"1"});

        if (execute >0){
            //对比成功（令牌正确），删除成功
            log.info("令牌验证成功");
            return true;
        }
        /*高并发会出现问题
        String val = redisTemplate.opsForValue().get(RedisConst.ORDER_TEMP_TOKEN + tradeNo);
        if (!StringUtils.isEmpty(val)){
            //令牌正确
            redisTemplate.delete(RedisConst.ORDER_TEMP_TOKEN + tradeNo);
            return true;
        }*/
        return false;
    }

    @Override
    public Long submitOrder(OrderSubmitVo submitVo, String tradeNo) {

        //1、验令牌
        if (!checkGenerateTradeNo(tradeNo)){
            throw new GmallException(ResultCodeEnum.TRADE_NO_ERROR);
        }

        //2、验库存
        List<String> noStockSkus = new ArrayList<>();
        submitVo.getOrderDetailList().forEach(orderDetail -> {
            String hasStock = wareFeignClient.hasStock(orderDetail.getSkuId(), orderDetail.getSkuNum());
            if (RedisConst.NO_STOCK.equals(hasStock)){
                noStockSkus.add(orderDetail.getSkuName());
            }
        });
        if (noStockSkus.size() > 0){

            String noStockSkuName = noStockSkus.stream().reduce((s1, s2) -> s1 + " " + s2).get();

            throw new GmallException(noStockSkuName + ResultCodeEnum.NO_STOCK_ERROR.getMessage(),ResultCodeEnum.NO_STOCK_ERROR.getCode());
        }


        //3、验价格
        submitVo.getOrderDetailList().forEach(orderDetail -> {
            BigDecimal realPrice = skuDetailFeignClient.getSkuPrice(orderDetail.getSkuId()).getData();
            BigDecimal orderPrice = orderDetail.getOrderPrice();
//            BigDecimal subtract = realPrice.subtract(orderPrice);
            if(!realPrice.equals(orderPrice)){
                throw new GmallException(ResultCodeEnum.PRICE_ERROR);
            }
        });

        //4、检验通过，保存数据
        Long orderId = orderInfoService.saveOrder(submitVo,tradeNo);

        //保存后删除购物车内选定的商品
        cartFeignClient.deleteChecked();
        //30分钟后未支付，关闭订单（mq消息队列 ）


        return orderId;
    }

    @Override
    public OrderInfo getOrderInfo(String orderId) {

        return orderInfoService.getById(Long.valueOf(orderId));
    }

    /**
     * 关闭订单
     * @param orderId
     * @param userId
     */
    @Override
    public void closeOrder(Long orderId, Long userId) {

        ProcessStatus closed = ProcessStatus.CLOSED;
        List<ProcessStatus> expected = Arrays.asList(ProcessStatus.UNPAID,ProcessStatus.FINISHED);

        orderInfoService.changeOrderStatus(orderId,userId,closed,expected);

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

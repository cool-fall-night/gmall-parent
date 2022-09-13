package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.common.vo.UserAuthInfo;
import com.atguigu.gmall.feign.product.SkuDetailFeignClient;
import com.atguigu.gmall.model.cart.CartInfo;
import com.atguigu.gmall.model.product.SkuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 毛伟臣
 * 2022/9/9
 * 8:55
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;
    @Autowired
    ThreadPoolExecutor executor;

    /**
     * 商品添加到购物车
     *
     * @param skuId
     * @param skuNum
     */
    @Override
    public SkuInfo addToCart(Long skuId, Integer skuNum) {


        //从线程中获取userId或userTempId，构建redis的userIdKey值
        String cartKey = determineCartKey();

        //给购物车添加商品
        SkuInfo skuInfo = addItemCart(skuId, skuNum, cartKey);
        UserAuthInfo authInfo = AuthContextHolder.getCurrentAuthInfo();

        if (authInfo.getUserId() == null) {
            String tempCartKey = RedisConst.CART_KEY_PREFIX + authInfo.getUserTempId();
            redisTemplate.expire(tempCartKey, 90, TimeUnit.DAYS);
        }

        return skuInfo;
    }

    /**
     * 获取购物车列表
     *
     * @param cartKey
     */
    @Override
    public List<CartInfo> getCartList(String cartKey) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        List<CartInfo> cartInfos = hashOps.values()
                .stream()
                .map(str -> Jsons.toObj(str, CartInfo.class))
                .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                .collect(Collectors.toList());

        //异步调用获取列表时更新所有商品的价格（使用线程池）
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        executor.submit(() -> {
            //绑定请求到这个线程
            RequestContextHolder.setRequestAttributes(requestAttributes);
            updateCartAllItemPrice(cartKey, cartInfos);
            RequestContextHolder.resetRequestAttributes();
        });


        return cartInfos;
    }

    /**
     * //获取列表时更新所有商品的价格
     *
     * @param cartKey
     * @param cartInfos
     */
    @Override
    public void updateCartAllItemPrice(String cartKey, List<CartInfo> cartInfos) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        cartInfos.forEach(cartInfo -> {
            //查出最新的商品价格
            Result<BigDecimal> price = skuDetailFeignClient.getSkuPrice(cartInfo.getSkuId());
            //设置最新价格
            cartInfo.setSkuPrice(price.getData());
            cartInfo.setUpdateTime(new Date());
            //重新存入
            hashOps.put(cartInfo.getSkuId().toString(), Jsons.toJsonStr(cartInfo));
        });
    }

    /**
     * 合并临时购物车
     */
    @Override
    public void mergeUserAndTempCart() {

        UserAuthInfo authInfo = AuthContextHolder.getCurrentAuthInfo();

        if (authInfo.getUserId() != null && !StringUtils.isEmpty(authInfo.getUserTempId())) {
            //可能需要合并
            String tempCartKey = RedisConst.CART_KEY_PREFIX + authInfo.getUserTempId();
            String userCartKey = RedisConst.CART_KEY_PREFIX + authInfo.getUserId();

            List<CartInfo> tempCartList = getCartList(tempCartKey);
            if (tempCartList != null && tempCartList.size() > 0) {
                //临时购物车中有商品，确定合并
                for (CartInfo info : tempCartList) {
                    Long skuId = info.getSkuId();
                    Integer skuNum = info.getSkuNum();
                    addItemCart(skuId, skuNum, userCartKey);
                    //清除临时购物车内商品
                    redisTemplate.opsForHash().delete(tempCartKey, skuId.toString());
                }
            }
        }
    }

    /**
     * 从线程中获取userId或userTempId，构建redis的userIdKey值
     */
    @Override
    public String determineCartKey() {

        UserAuthInfo info = AuthContextHolder.getCurrentAuthInfo();

        String cartKey = RedisConst.CART_KEY_PREFIX;

        Long userId = info.getUserId();
        if (!StringUtils.isEmpty(userId)) {
            cartKey = cartKey + "" + userId;
        } else {
            String userTempId = info.getUserTempId();
            cartKey = cartKey + "" + userTempId;
        }
        return cartKey;
    }

    @Override
    public void updateItemNum(Long skuId, Integer num, String cartKey) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        CartInfo item = getCartInfo(skuId, hashOps);
        item.setSkuNum(item.getSkuNum() + num);
        item.setUpdateTime(new Date());

        hashOps.put(skuId.toString(), Jsons.toJsonStr(item));

    }

    @Override
    public void updateCheckCart(Long skuId, Integer isChecked, String cartKey) {
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        CartInfo item = getCartInfo(skuId, hashOps);
        item.setIsChecked(isChecked);

        hashOps.put(skuId.toString(), Jsons.toJsonStr(item));
    }

    @Override
    public void deleteCart(Long skuId, String cartKey) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        hashOps.delete(cartKey, skuId.toString());
    }

    @Override
    public void deleteChecked(String cartKey) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        List<String> idList = getCheckedItem(cartKey)
                .stream()
                .map(cartInfo -> cartInfo.getSkuId().toString())
                .collect(Collectors.toList());

        if (idList.size() > 0){
            hashOps.delete(idList.toArray());
        }
    }

    @Override
    public List<CartInfo> getCheckedItem(String cartKey) {

        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);

        List<CartInfo> cartInfos = hashOps.values()
                .stream()
                .map(str -> Jsons.toObj(str, CartInfo.class))
                .sorted((o1, o2) -> o2.getCreateTime().compareTo(o1.getCreateTime()))
                .collect(Collectors.toList());

        return cartInfos
                .stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .collect(Collectors.toList());
    }

    /**
     * 根据cartInfo转换为skuInfo
     *
     * @param cartInfo
     */
    private SkuInfo makeSkuInfoByCartInfo(CartInfo cartInfo) {

        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setSkuName(cartInfo.getSkuName());
        skuInfo.setSkuDefaultImg(cartInfo.getImgUrl());
        skuInfo.setId(cartInfo.getSkuId());

        return skuInfo;
    }

    /**
     * 根据skuInfo生成cartInfo
     *
     * @param skuInfo
     */
    private CartInfo makeCartInfoBySkuInfo(SkuInfo skuInfo) {

        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuInfo.getId());
        cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setIsChecked(1);
        cartInfo.setCreateTime(new Date());
        cartInfo.setUpdateTime(new Date());
        cartInfo.setCartPrice(skuInfo.getPrice());
        cartInfo.setSkuPrice(skuInfo.getPrice());

        return cartInfo;
    }

    /**
     * 给购物车添加商品
     *
     * @param skuId
     * @param skuNum
     * @param cartKey
     * @return
     */
    private SkuInfo addItemCart(Long skuId, Integer skuNum, String cartKey) {

        //key(cartKey):hash(skuId-skuInfo)
        //拿到购物车
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(cartKey);
        String skuIdStr = skuId.toString();
        if (!hashOps.hasKey(skuIdStr)) {
            //该购物车内没有此商品
            //获取当前购物车的商品数量
            Long itemSize = hashOps.size();
            if (itemSize >= RedisConst.CART_MAX_SIZE) {
                throw new GmallException(ResultCodeEnum.CART_OVERFLOW);
            }
            //远程调用查询商品信息
            SkuInfo skuInfo = skuDetailFeignClient.getSkuInfo(skuId).getData();
            //根据skuId和skuNum创建cartInfo
            CartInfo cartInfo = makeCartInfoBySkuInfo(skuInfo);
            cartInfo.setSkuNum(skuNum);
            //存入购物车
            hashOps.put(skuIdStr, Jsons.toJsonStr(cartInfo));
            return skuInfo;
        } else {
            //购物车中已存在此商品
            //获取此商品信息
            CartInfo cartInfo = getCartInfo(skuId, hashOps);
            //查询商品实时价格
            BigDecimal skuPrice = skuDetailFeignClient.getSkuPrice(skuId).getData();
            //修改商品相关信息
            cartInfo.setSkuNum(cartInfo.getSkuNum() + skuNum);
            cartInfo.setSkuPrice(skuPrice);
            cartInfo.setUpdateTime(new Date());
            //存入购物车
            hashOps.put(skuIdStr, Jsons.toJsonStr(cartInfo));

            //根据cartInfo转换为skuInfo
            SkuInfo skuInfo = makeSkuInfoByCartInfo(cartInfo);
            return skuInfo;
        }
    }

    private CartInfo getCartInfo(Long skuId, BoundHashOperations<String, String, String> hashOps) {

        String skuIdStr = skuId.toString();

        return Jsons.toObj(hashOps.get(skuIdStr), CartInfo.class);

    }
}

package com.atguigu.gmall.feign.user;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 23:26
 * @version 1.0
 * @since JDK1.8
 */

@FeignClient("service-user")
@RequestMapping("/api/inner/rpc/user")
public interface UserFeignClient {

    /**
     * 远程调用获取用户收获地址列表
     * @return
     */
    @GetMapping("/getUserAddressList")
    Result<List<UserAddress>> getUserAddressList();

}

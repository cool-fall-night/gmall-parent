package com.atguigu.gmall.user.api;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.util.AuthContextHolder;
import com.atguigu.gmall.model.user.UserAddress;
import com.atguigu.gmall.user.service.UserAddressService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/9/13
 * 23:27
 * @version 1.0
 * @since JDK1.8
 */
@Api(tags = "用户信息检索的API")
@RestController
@RequestMapping("/api/inner/rpc/user")
public class UserApiController {

    @Autowired
    UserAddressService userAddressService;

    @GetMapping("/getUserAddressList")
    Result<List<UserAddress>> getUserAddressList(){

        Long userId = AuthContextHolder.getCurrentAuthInfo().getUserId();

        QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<UserAddress> userAddressList = userAddressService.list(wrapper);

        return Result.ok(userAddressList);
    }
}

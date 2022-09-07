package com.atguigu.gmall.user.service;

import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.LoginSuccessVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 毛伟臣
* @description 针对表【user_info(用户表)】的数据库操作Service
* @createDate 2022-09-07 13:29:21
*/
public interface UserInfoService extends IService<UserInfo> {

    LoginSuccessVo login(UserInfo userInfo);

    void logout(String token);
}


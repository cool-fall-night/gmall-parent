package com.atguigu.gmall.common.util;


import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.vo.UserAuthInfo;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取登录用户信息类
 */
public class AuthContextHolder {


    public static UserAuthInfo getCurrentAuthInfo() {


        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        UserAuthInfo authInfo = new UserAuthInfo();
        String header = request.getHeader(RedisConst.USERID_HEADER);

        if (!StringUtils.isEmpty(header)){

            authInfo.setUserId(Long.parseLong(header));
        }
        authInfo.setUserTempId(request.getHeader(RedisConst.USERTEMPID_HEADER));
        return authInfo;
    }
}

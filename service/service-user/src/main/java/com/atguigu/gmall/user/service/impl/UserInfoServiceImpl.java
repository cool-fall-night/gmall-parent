package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.common.constant.RedisConst;
import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.common.util.MD5;
import com.atguigu.gmall.model.user.UserInfo;
import com.atguigu.gmall.model.vo.LoginSuccessVo;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 毛伟臣
 * @description 针对表【user_info(用户表)】的数据库操作Service实现
 * @createDate 2022-09-07 13:29:21
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginSuccessVo login(UserInfo userInfo) {

        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getLoginName, userInfo.getLoginName())
               .eq(UserInfo::getPasswd, MD5.encrypt(userInfo.getPasswd()));

        UserInfo info = userInfoMapper.selectOne(wrapper);
        LoginSuccessVo vo = new LoginSuccessVo();

        if (!StringUtils.isEmpty(info)){
            //登录成功
            vo.setNickName(info.getNickName());
            String token = UUID.randomUUID().toString().replace("-","");
            vo.setToken(token);

            //保存到redis中
            redisTemplate.opsForValue().set(RedisConst.USER_INFO_PREFIX + token, Jsons.toJsonStr(info),7, TimeUnit.DAYS);

            return vo;
        }
        return null;
    }

    @Override
    public void logout(String token) {

        redisTemplate.delete(token);
    }
}





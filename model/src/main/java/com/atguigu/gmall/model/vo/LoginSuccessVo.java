package com.atguigu.gmall.model.vo;

import lombok.Data;

/**
 * @author 毛伟臣
 * 2022/9/7
 * 18:20
 * @version 1.0
 * @since JDK1.8
 */
@Data
public class LoginSuccessVo {

    private String token;

    private String nickName;
}

package com.atguigu.gmall.pay.config;

import com.alipay.api.AlipayConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 毛伟臣
 * 2022/9/16
 * 20:24
 * @version 1.0
 * @since JDK1.8
 */

@Data
@Component
@ConfigurationProperties(prefix = "app.alipay")
public class AlipayProperties {
    //应用ID
    private String appId;
    // 支付宝网关
    private String gatewayUrl;
    // 商户私钥
    private String merchantPrivateKey;
    // 字符编码格式
    private String charset;
    // 支付宝公钥
    private String alipayPublicKey;
    // 签名方式
    private String signType;
    // 服务器异步通知页面路径
    private String notifyUrl;
    // 页面跳转同步通知页面路径
    private String returnUrl;
}

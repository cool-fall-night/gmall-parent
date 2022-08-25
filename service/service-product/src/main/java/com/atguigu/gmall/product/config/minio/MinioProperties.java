package com.atguigu.gmall.product.config.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 21:58
 * @version 1.0
 * @since JDK1.8
 */

@Data
@Component
@ConfigurationProperties(prefix = "app.minio")
public class MinioProperties {

    String endpoint;

    String accessKey;

    String secretKey;

    String bucketName;
}



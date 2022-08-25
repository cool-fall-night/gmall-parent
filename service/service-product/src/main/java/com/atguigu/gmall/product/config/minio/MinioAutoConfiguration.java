package com.atguigu.gmall.product.config.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 21:47
 * @version 1.0
 * @since JDK1.8
 */

@Configuration
public class MinioAutoConfiguration {

    @Autowired
    MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = new MinioClient(
                minioProperties.getEndpoint(),
                minioProperties.getAccessKey(),
                minioProperties.getSecretKey());

        String bucketName = minioProperties.getBucketName();

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(bucketName);
        if (isExist) {
            System.out.println(bucketName + "already exists.");
        } else {
            // 创建一个名为gmall-resource的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket(bucketName);
        }

        return minioClient;
    }


}

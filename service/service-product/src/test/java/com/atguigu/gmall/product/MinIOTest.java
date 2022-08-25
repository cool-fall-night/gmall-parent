package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 20:16
 * @version 1.0
 * @since JDK1.8
 */


@SpringBootTest     //SpringBoot组件测试注解
public class MinIOTest {


    /**
     * minIO时间敏感中间件
     * 上传时间与系统时间不一致会报错误
     * ErrorResponse(code = RequestTimeTooSkewed,
     * message = The difference between the request time and the server's time is too large.,
     * bucketName = null, objectName = null, resource = /gmall-resource, requestId = null,
     * hostId = ab182dcc-7fb4-48b7-a914-32936e7e7d68)
     */
    @Test
    public void uploadFile() throws Exception {
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.6.66:9000",
                                                     "admin",
                                                     "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall-resource");
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为gmall-resource的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("gmall-resource");
            }

            FileInputStream stream = new FileInputStream("D:\\System\\Pictures\\杂图\\96029563_p0.jpg");
            PutObjectOptions options = new PutObjectOptions(stream.available(), -1L);
            options.setContentType("jpg");
            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("gmall-resource","雷电将军",stream,options);
            System.out.println("上传成功");
        } catch (MinioException e) {
            System.err.println("上传失败" + e);
        }
    }
}

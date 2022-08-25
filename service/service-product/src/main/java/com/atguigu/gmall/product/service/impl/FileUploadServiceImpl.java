package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.minio.MinioAutoConfiguration;
import com.atguigu.gmall.product.config.minio.MinioProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 20:58
 * @version 1.0
 * @since JDK1.8
 */

@Service
public class FileUploadServiceImpl implements FileUploadService {


    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String fileUpload(MultipartFile file) throws Exception {

        String endpoint = minioProperties.getEndpoint();
        String bucketName = minioProperties.getBucketName();

        String originalFilename = file.getOriginalFilename();
        String contentTypePath = "Picture";
        String date = DateUtil.formatDate(new Date());

        String filename = contentTypePath + "/" + date + "/" + UUID.randomUUID().toString().replace("-","") + "-" + originalFilename;
        String contentType = file.getContentType();
        InputStream inputStream = file.getInputStream();
        long size = file.getSize();

        PutObjectOptions options = new PutObjectOptions(size,-1L);
        options.setContentType(contentType);

        minioClient.putObject(bucketName,filename,inputStream,options);
        String url = endpoint + "/" + bucketName + "/" + filename;

        return url;
    }
}

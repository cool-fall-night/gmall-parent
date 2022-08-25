package com.atguigu.gmall.product.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 毛伟臣
 * 2022/8/25
 * 20:58
 * @version 1.0
 * @since JDK1.8
 */

public interface FileUploadService {
    String fileUpload(MultipartFile file) throws Exception;
}

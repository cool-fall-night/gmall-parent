package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 毛伟臣
 * 2022/8/24
 * 0:49
 * @version 1.0
 * @since JDK1.8
 */

@Api(tags = "文件上传模块")
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {


    @Autowired
    private FileUploadService fileUploadService;

    /**
     * 文件上传
     * 请求方式：POST
     * 文件流放在请求体中
     * @return
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestParam("file")MultipartFile file) throws Exception {

        String url = fileUploadService.fileUpload(file);

        return Result.ok(url);
    }
}

package com.atguigu.gmall.common.handler;

import com.atguigu.gmall.common.execption.GmallException;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.common.result.ResultCodeEnum;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    /**
     * 自定义异常处理方法
     * @param
     * @return
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result error(GmallException gmallException){
        Result<Object> fail = Result.fail();
        fail.setCode(gmallException.getCode());
        fail.setMessage(gmallException.getMessage());
        return fail;

    }
}

package com.atguigu.gmall.common.feign;

import feign.RetryableException;
import feign.Retryer;

/**
 * @author 毛伟臣
 * 2022/9/2
 * 20:45
 * @version 1.0
 * @since JDK1.8
 */

public class MyRetryer implements Retryer {
    @Override
    public void continueOrPropagate(RetryableException e) {
        throw e;
    }

    @Override
    public Retryer clone() {
        return this;
    }
}

package com.atguigu.gmall.model.list;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 毛伟臣
 * 2022/9/6
 * 19:32
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class OrderMapVo implements Serializable {
    //排序类型（1：综合、2：价格）
    private String type;
    //排序方式（asc：升序、desc：降序）
    private String sort;
}

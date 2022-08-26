package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * @author 毛伟臣
 * 2022/8/26
 * 21:43
 * @version 1.0
 * @since JDK1.8
 */

@Data
public class CategoryTreeTo {

    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;
}

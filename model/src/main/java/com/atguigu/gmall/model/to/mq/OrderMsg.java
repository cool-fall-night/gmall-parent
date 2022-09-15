package com.atguigu.gmall.model.to.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 毛伟臣
 * 2022/9/15
 * 22:43
 * @version 1.0
 * @since JDK1.8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderMsg {

    private Long orderId;
    private Long userId;
}

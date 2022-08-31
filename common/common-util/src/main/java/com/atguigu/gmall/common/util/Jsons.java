package com.atguigu.gmall.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author 毛伟臣
 * 2022/8/28
 * 18:06
 * @version 1.0
 * @since JDK1.8
 */

public class Jsons {

    //线程安全
    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 把object对象转为Json字符串
     * @param object
     * @return
     */
    public static String toJsonStr(Object object) {

        try {
            String asString = mapper.writeValueAsString(object);
            return asString;
        } catch (JsonProcessingException e) {
            return null;
        }
    }


    /**
     * 把Json字符串转为object对象
     * @param jsonStr
     * @param clz
     * @param <T>
     * @return
     */
    public static<T> T toObj(String jsonStr, Class<T> clz) {

        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }
        T t = null;

        try {
            t = mapper.readValue(jsonStr,clz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }
}

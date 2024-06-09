package com.tree3.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/8 15:46 </p>
 */
@Slf4j
public class JSONUtils {

    /**
     * 获取对象的json字符串
     *
     * @param obj
     * @return
     */
    public static String toJsonStr(Object obj) {
        String jsonStr = null;
        try {
            jsonStr = new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * json字符串 转 自定义对象
     *
     * @param jsonStr
     * @param klass
     * @param <T>
     * @return
     */
    public static <T> T paresToObj(String jsonStr, Class<T> klass) {
        if (jsonStr == null || "".equals(jsonStr) || klass == null) {
            return null;
        }
        try {
            return klass.equals(String.class) ? (T) jsonStr : new ObjectMapper().readValue(jsonStr, klass);
        } catch (Exception e) {
            log.error("JSON字符串转{}对象失败:{}", klass, e);
            throw new RuntimeException("JSON字符串转" + klass + "对象失败");
        }
    }


}

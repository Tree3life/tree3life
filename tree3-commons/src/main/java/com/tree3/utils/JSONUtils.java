package com.tree3.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/8 15:46 </p>
 */
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
}

package com.tree3.config;

import com.tree3.sugar.boleresp.ResponseResultHandler;
import com.tree3.sugar.boleresp.ResponseResultInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 11:18 </p>
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    public static Logger logger = LoggerFactory.getLogger(WebConfiguration.class);

    /**
     * 方案一：
     * 注册@ResponseResult拦截器
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        logger.debug(" 注册@ResponseResult拦截器");
//        //自定义的请求拦截器：检查请求方法上是否被@ResponseResult标识
//        ResponseResultInterceptor responseResultInterceptor = new ResponseResultInterceptor();
//        //统一结果封装
//        registry.addInterceptor(responseResultInterceptor);
//    }

//    @Bean
//    public ResponseResultHandler responseResultHandler() {
//        logger.debug(" 注册用于封装结果的切面 ");
//        return new ResponseResultHandler();
//    }
}

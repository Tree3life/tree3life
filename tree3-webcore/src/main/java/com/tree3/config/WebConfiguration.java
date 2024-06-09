package com.tree3.config;

import com.tree3.utils.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

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
    @Resource
    private RedisCache redisCache;

    // fixme：放开此处注释 (Rupert，2024/6/7 9:12)
    /**
     * 除了【excludePathPatterns中包含的几个接口外】所有的请求都需要进行认证
     *
     * @param registry
     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        logger.debug(" 注册AuthInterceptor拦截器");
//        AuthInterceptor authInterceptor = new AuthInterceptor();
//        registry.addInterceptor(new RefreshTokenInterceptor(redisCache)).addPathPatterns(
//                "/**"
//        ).order(1);
//        registry.addInterceptor(authInterceptor)
//                //方向不必要的拦截
//                .excludePathPatterns(
//                        "/login/**"
//                        , "/auth/**"
//                        , "/logout/**"
//                        , "/signIn/**"
//                )
//                //要位于 RefreshTokenInterceptor 拦截器之后
//                .order(2);
//    }
}

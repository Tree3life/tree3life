package com.tree3.sugar.boleresp;

import java.lang.annotation.*;

/**
 * 被本注解修饰的方法 会对对返回结果进行包装，修饰
 * note：本功能的实现 有两种方案
 * 方案一： @ControllerAdvice 搭配 拦截器实现 {@link ResponseResultHandler}
 * 方案二：仅用 @ControllerAdvice 实现
 * 注意：当返回结果的类型为{@link org.springframework.http.ResponseEntity}时，本注解无效
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface PackageResponse {

}

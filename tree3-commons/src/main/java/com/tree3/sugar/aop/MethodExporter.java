package com.tree3.sugar.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 使用环绕通知对方法进行增强
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/21 19:21 </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodExporter {
}

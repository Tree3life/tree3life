package com.tree3.exception;

import com.tree3.sugar.boleresp.ResponseResultHelper;
import com.tree3.utils.BusinessResponseCode;
import com.tree3.utils.ExceptionUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Web全局异常处理器
 * {@link org.springframework.web.bind.annotation.RestControllerAdvice}
 * 是Spring Boot.的拦截器，会拦截  一切关注的异常
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 9:51 </p>
 */
@Slf4j
@RestControllerAdvice
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class GlobalExceptionHandler {
    @Value("${app.showCase}")
    private boolean showCase = true;


    /**
     * 对象绑定异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    public Object handleBindException(BindException e, HttpServletRequest request) {
        loggingCaseInfo(e, "对象绑定异常");
        return ResponseResultHelper.fail(BusinessResponseCode.PARAM_TYPE_BIND_ERROR, e.getMessage());
    }


    /**
     * 方法接收参数阶段的异常
     * 备注：发生这类异常的请求，在接收参数的阶段就出现了异常，是无法进入方法内部执行的
     * {@link org.springframework.beans.TypeMismatchException}是{@link org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException}和 {@link org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException}
     * 的父类
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException(TypeMismatchException e) {
        loggingCaseInfo(e, "方法接收参数阶段异常");
        return ResponseResultHelper.fail(BusinessResponseCode.PARAM_HANDLE_ERROR, e.getMessage());
    }

    /**
     * 业务逻辑异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException e) {
        loggingCaseInfo(e, "业务逻辑异常");
        //BusinessException 对象 本身就聚合了  BusinessResponseCode对象
        return ResponseResultHelper.fail(e.getBusinessResponseCode(), e.getMessage());
    }


    /**
     * 暂时未使用
     * 捕获处理404异常
     * 搭配配置文件中的配置使用
     * `spring.application.name.微服务名称.mvc.throw-exception-if-no-handler-found: true
     * spring.web.resources.add-mappings: false`
     * @param e
     * @return
     */
//    @ExceptionHandler(value = NoHandlerFoundException.class)
//    public ResponseResultHelper handleNoHandlerFoundException(NoHandlerFoundException e) {
//        loggingCaseInfo(e, "NoHandlerFoundException 缺少相应处理器");
//        return ResponseResultHelper.fail(BusinessResponseCode.NOT_FOUND, e.getMessage());
//    }

    /**
     * 捕获所有运行时异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = RuntimeException.class)
    public Object handleRuntimeException(RuntimeException e) {
        System.out.println("捕获所有运行时异常");
//        loggingCaseInfo(e, "运行时异常");
        return ResponseResultHelper.fail(HttpStatus.NO_CONTENT,BusinessResponseCode.UNKNOWN_EXCEPTION,e.getMessage());
    }


    /**
     * 打印异常信息
     * 使用ExceptionUtil中的方法也能实现
     *
     * @param e
     * @param msg
     */
    private void loggingCaseInfo(Throwable e, String msg) {
        if (showCase) {
            log.debug("Web全局异常处理");
            log.debug(msg + "===========>：" + e.getMessage());
            log.error(ExceptionUtil.getMessage(e));
        }
    }

}

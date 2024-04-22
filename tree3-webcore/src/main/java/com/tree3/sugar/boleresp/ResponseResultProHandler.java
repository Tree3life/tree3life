package com.tree3.sugar.boleresp;

import com.tree3.exception.BusinessException;
import com.tree3.utils.BusinessResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 统一处理返回结果的切面（Aop）
 * 方案二
 * 处理响应报文，对被@ResponseHandler标注的方法返回结果进行 再封装
 *
 * @Author: Jinhui
 * @Date 2021/11/12 14:51
 */
@ControllerAdvice
public class ResponseResultProHandler implements ResponseBodyAdvice<Object> {

    private Logger logger = LoggerFactory.getLogger(ResponseResultHandler.class);

    /**
     * 对beforeBodyWrite()的执行者进行筛选
     *
     * @param returnType
     * @param converterType
     * @return true：执行beforeBodyWrite()；false：不执行beforeBodyWrite()
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        boolean supports = false;
        supports = (
                //判断方法上是否被 注解标注
                returnType.getMethodAnnotation(PackageResponse.class) != null ||
                        //判断类上上是否被 包含该注解
                        AnnotationUtils.findAnnotation(returnType.getContainingClass(), PackageResponse.class) != null
        );
        return supports;
    }

    /**
     * 对返回结果进行再封装（统一封装） 为RespResult
     * RespResultDemo --> 直接传递
     * 其它-->RespResultDemo
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        logger.debug("pro封装返回结果       ");

        if (body instanceof ResponseResultHelper) {
            return body;
        }

        /**
         * SpringMVC中后期可能会对String类型的返回值做特殊处理，因此不对String类型的返回值做任何处理
         * 如果有向前台返回一个String类型的需求：可以通过直接该控制器的返回值类型设置为RespResult，直接调用RespResult.success(body)
         */
        if (body instanceof String) {
            return body;
        }
        if (body instanceof ModelAndView) {
            throw new BusinessException(BusinessResponseCode.MEANINGLESS_USE, "在返回值类型为ModelAndView的方法上使用@ResponseResult是无意义的！");
        }

        //进行数据封装
        return ResponseResultHelper.success(body);
    }
}

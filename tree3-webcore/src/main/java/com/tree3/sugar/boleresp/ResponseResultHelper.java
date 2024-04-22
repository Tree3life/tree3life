package com.tree3.sugar.boleresp;

import com.tree3.utils.BusinessResponseCode;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * <p>
 * 对返回结果进行封装，继承自{@link org.springframework.http.ResponseEntity}
 * 可以 对 http响应码和 业务码 进行灵活 控制
 * <p>
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 9:11 </p>
 */
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class ResponseResultHelper implements Serializable {

    private static final long serialVersionUID = 9134166006868700005L;
    private Integer bizCode;
    private String bizCodeText;
    private String message;
    private Object data;

    /**
     * 创建 成功状态的 响应对象
     */
    public ResponseResultHelper() {
        this.bizCode = BusinessResponseCode.SUCCESS.code();
        this.bizCodeText = BusinessResponseCode.SUCCESS.message();
        this.message = BusinessResponseCode.SUCCESS.message();
    }

    /**
     * 创建 成功状态的 响应对象
     *
     * @param data
     */
    public ResponseResultHelper(Object data) {
        this.bizCode = BusinessResponseCode.SUCCESS.code();
        this.bizCodeText = BusinessResponseCode.SUCCESS.message();
        this.message = BusinessResponseCode.SUCCESS.message();
        this.data = data;
    }

    /**
     * 创建自定义状态的 响应对象
     *
     * @param httpStatus
     * @param businessCode
     */
    public ResponseResultHelper(HttpStatus httpStatus, BusinessResponseCode businessCode) {
        this.bizCode = businessCode.code();
        this.bizCodeText = businessCode.message();
    }


    /**
     * 创建 服务器内部异常 的响应对象
     *
     * @param businessCode
     * @param errorInfo
     */
    public ResponseResultHelper(BusinessResponseCode businessCode, String errorInfo) {
        this.bizCode = businessCode.code();
        this.bizCodeText = businessCode.message();
        this.message = errorInfo;
    }

    /**
     * @param businessCode
     */
    public ResponseResultHelper(BusinessResponseCode businessCode, Object data) {
        this.bizCode = businessCode.code();
        this.bizCodeText = businessCode.message();
        this.message = businessCode.message();
        this.data = data;
    }

    public ResponseResultHelper(BusinessResponseCode businessCode, Object data, String message) {
        this.bizCode = businessCode.code();
        this.bizCodeText = businessCode.message();
        this.message = message;
        this.data = data;
    }

    /**
     * @return 正常的响应结果
     */
    public static ResponseEntity<Object> success(Object data) {
        return new ResponseEntity<Object>(new ResponseResultHelper(BusinessResponseCode.SUCCESS, data), HttpStatus.OK);
    }


    /**
     * @param strData 字符串形式的响应结果
     * @return
     */
    public static ResponseEntity<Object> successStr(String strData) {
        return success(strData);
    }


    /**
     * @return 正常的响应结果（可自定义消息）
     */
    public static ResponseEntity<Object> success(Object data, String message) {
        return new ResponseEntity<Object>(new ResponseResultHelper(BusinessResponseCode.SUCCESS, data, message), HttpStatus.OK);
    }

    public static ResponseEntity<Object> successStr(String strData, String message) {
        return success(strData, message);
    }


    /**
     * 构建失败的响应结果
     *
     * @return 异常或失败的响应结果
     */
    public static ResponseEntity<Object> fail(String errorInfo) {
        ResponseResultHelper resultHelper = new ResponseResultHelper();
        resultHelper.setBizCode(BusinessResponseCode.UNKNOWN_EXCEPTION.code());
        resultHelper.setBizCodeText(BusinessResponseCode.UNKNOWN_EXCEPTION.message());
        resultHelper.setMessage(errorInfo);
        return new ResponseEntity<>(resultHelper, HttpStatus.OK);

    }

    /**
     * 构建失败的响应结果
     *
     * @return 异常或失败的响应结果
     */
    public static ResponseEntity<Object> fail(BusinessResponseCode businessResponseCode, String errorInfo) {
        ResponseResultHelper resultHelper = new ResponseResultHelper();
        resultHelper.setBizCode(businessResponseCode.code());
        resultHelper.setBizCodeText(businessResponseCode.message());
        resultHelper.setMessage(errorInfo);
        return new ResponseEntity<>(resultHelper, HttpStatus.OK);

    }

    /**
     * 自由度最高的 构建失败响应结果 的方法
     *
     * @return 异常或失败的响应结果
     */
    public static ResponseEntity<Object> fail(HttpStatus httpStatus, BusinessResponseCode businessResponseCode, String errorInfo) {
        ResponseResultHelper resultHelper = new ResponseResultHelper();
        resultHelper.setMessage(errorInfo);
        resultHelper.setBizCode(businessResponseCode.code());
        resultHelper.setBizCodeText(businessResponseCode.message());
        return new ResponseEntity<Object>(resultHelper, httpStatus);
    }

}

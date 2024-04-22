package com.tree3.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.utils.BusinessResponseCode;
import lombok.*;

/**
 * <p>
 * 业务异常
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/4/20 9:39 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 3242040843439353332L;

    private BusinessResponseCode businessResponseCode;

    /**
     * 编译时异常
     * @param errorInfo
     */
    public BusinessException(String errorInfo) {
        super(errorInfo);
    }

    public BusinessException(BusinessResponseCode businessResponseCode) {
        super(businessResponseCode.message());
        this.businessResponseCode = businessResponseCode;
    }

    /**
     * 自定义错误信息
     *
     * @param businessResponseCode
     * @param errorInfo
     */
    public BusinessException(BusinessResponseCode businessResponseCode, String errorInfo) {
        super(errorInfo);
        this.businessResponseCode = businessResponseCode;
    }
}

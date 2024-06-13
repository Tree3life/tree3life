package com.tree3.pojo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/**
 * <p>
 * 文本消息
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 11:17 </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class MessageText extends MessageHead {

    private static final long serialVersionUID = 827966767228404102L;

//    private Integer id;

    /**
     * 消息内容
     */
    private String content;

    @Override
    public int queryMessageType() {
        return MessageText;
    }
}

package com.tree3.pojo.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 聊天消息对象
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 10:25 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 4896335188770048182L;


    public abstract int queryMessageType();


    public static Class<?> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private static final Map<Integer, Class<?>> messageClasses = new HashMap<>();

    //region 消息状态
    //未送达
    public static final int Status_Undelivered = -1;
    //已送达&未读
    public static final int Status_Delivered = 0;
    //已读
    public static final int Status_Unread = 1;
    //已读
    public static final int Status_Read = 2;
    //撤回
    public static final int Status_Revocation = 3;
    //endregion 消息状态

    //region 消息类型
    //消息头：消息的控制
    public static final int MessageHead = -1;
    //文本消息
    public static final int MessageText = 0;
    //文件消息
    public static final int MessageFile = 1;
    //响应消息
    public static final int MessageResponse = 2;

    static {
        messageClasses.put(MessageHead, MessageHead.class);
        messageClasses.put(MessageText, MessageText.class);
        messageClasses.put(MessageFile, MessageFile.class);
        messageClasses.put(MessageResponse, MessageResponse.class);
    }
    //endregion 消息类型


}

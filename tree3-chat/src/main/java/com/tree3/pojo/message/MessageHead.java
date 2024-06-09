package com.tree3.pojo.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.pojo.command.Command;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * websocket 消息头
 * | ---------------------  | ----------------------------------------------------|
 * | sendTime 发件时间 date  | messageType消息类型  int                              |
 * | state 消息状态 int      | deliveryTime 送达/收件时间 date（随消息状态进行改变）(可选)|
 * | from 发件人id          | to 收件人id/群id                                      |
 * |---------------------  | ----------------------------------------------------|
 * |contentType消息体类型 int| content  消息体 Object  由具体Message类决定是否实现      |
 * | --------------------- | ----------------------------------------------------|
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 8:06 </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class MessageHead extends Message {
    private static final long serialVersionUID = 5623169302863133290L;

    /**
     * 发送时间
     */
    private Date createTime;



    /**
     * 命令 {@link Command}
     */
    private Integer commandType;

    /**
     * 已送达/未送达
     * 已读/未读
     * 撤回
     */
    private Integer state;

    /**
     * 送达时间
     */
    private Date deliveryTime;

    /**
     * 发消息人的id
     */
    private Integer from;

    /**
     * 收消息人的id
     */
    private Integer to;

    /**
     * 用于传参数
     */
    private Object payload;



    @Override
    public int queryMessageType() {
        return MessageHead;
    }

    public static void main(String[] args) {
        com.tree3.pojo.message.MessageHead head = new MessageHead();
    }
}

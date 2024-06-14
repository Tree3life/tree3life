package com.tree3.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.pojo.command.Command;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天历史记录(ChatHistory)表
 *
 * @author rupert
 * @since 2024-05-20 16:19:13
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatHistory implements Serializable {

    private static final long serialVersionUID = 528614877599725476L;

    private Integer id;

    /**
     * 用于区分消息类型：{@link Command}
     */
    private Integer command;

    /**
     * 消息状态
     * 私聊信息：直接通过该字段进行标记
     * 群聊消息的未读功能：每个用户在下线的时候 要发送一条 下线消息
     * 通知系统它已经下线，记录其下线的时间，以该时间点为 参照 ，所有在该时间点之后的 群消息对该用户来说都是 未读的
     */
    private Integer state;

    /**
     * 发件人id
     */
    private Integer from;

    /**
     * 收件人id/群id
     */
    private Integer to;

    /**
     * 内容类型
     */
    private Integer contentType;

    /**
     * 消息内容，如果是文件类型，本字段存储该文件在文件服务器中的地址
     */
    private String content;

    /**
     * 创建时间、发送时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 送达时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 逻辑删除时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedTime;

    /**
     * 逻辑删除
     */
    private Boolean deleted;


    public ChatHistory(Integer command, Integer state, Integer from, Integer to,Date createTime, Boolean deleted) {
        this.command = command;
        this.state = state;
        this.from = from;
        this.to = to;
        this.createTime = createTime;
        this.deleted = deleted;
    }
}


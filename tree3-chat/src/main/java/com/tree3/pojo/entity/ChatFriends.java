package com.tree3.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 朋友
 * (ChatFriends)表
 *
 * @author rupert
 * @since 2024-05-20 16:19:50
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatFriends implements Serializable {

    private static final long serialVersionUID = 725590488676091859L;

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户对好友进行的分类id
     */
    private Integer categoryId;

    /**
     * 好友的id
     */
    private Integer friendId;

    /**
     * 逻辑删除
     */
    private Integer deleted;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deletedTime;

    /**
     * 对好友的备注名称
     */
    private String remarkName;


}


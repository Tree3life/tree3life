package com.tree3.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.pojo.entity.ChatFriends;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.entity.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/19 22:32 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatFriendDTO extends ChatFriends {
    private static final long serialVersionUID = 104312542179879949L;

    /**
     * 好友信息
     */
    private User friend;

    /**
     * 最新的一条聊天消息
     */
    private ChatHistory lastHistory;

    /**
     * 未读消息
     */
    private ArrayList<Integer> countUnread;

    /**
     * 聊天记录
     */
    private List<ChatHistory> histories;

}

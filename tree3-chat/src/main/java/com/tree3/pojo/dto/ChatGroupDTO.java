package com.tree3.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.pojo.entity.ChatGroup;
import com.tree3.pojo.entity.User;
import lombok.*;

import java.util.List;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/21 8:26 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatGroupDTO extends ChatGroup {
    private static final long serialVersionUID = 6418133244025178543L;

    /**
     * 群成员信息
     */
    private List<User> members;

    /**
     * 聊天记录
     */
    private List<ChatHistoryDTO> histories;

    /**
     * 最新的一条群聊消息
     */
    private ChatHistoryDTO lastHistory;
}

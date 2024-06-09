package com.tree3.pojo.qo;

import com.tree3.pojo.entity.ChatFriends;
import lombok.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/22 16:51 </p>
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatFriendsQuery extends ChatFriends {
    private static final long serialVersionUID = 3487335015412096348L;
    /**
     * 查询聊天消息 时使用
     * {@link com.tree3.pojo.command.Command}
     */
    private Integer cmd;


    /**
     * 离线消息的标记
     * {@link com.tree3.pojo.command.Command}-Logout(-2)
     */
    private Integer cmdLogout;

    /**
     * 聊天消息的状态{@link com.tree3.pojo.message.Message}-Status_Read
     */
    private Integer msgState;
}

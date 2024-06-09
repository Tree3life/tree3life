package com.tree3.pojo.qo;

import com.tree3.pojo.command.Command;
import com.tree3.pojo.entity.ChatGroup;
import lombok.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/21 9:18 </p>
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatGroupVo extends ChatGroup {

    private static final long serialVersionUID = -5037068667631319928L;

    private Integer userId;

    /**
     * 默认查找的是 文字群聊信息
     */
    private Integer command = Command.GroupChatText.getType();

}

package com.tree3.pojo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.entity.User;
import lombok.*;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/21 10:34 </p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatHistoryDTO extends ChatHistory {

    private static final long serialVersionUID = -3397048018621828930L;
    private User fromUser;
}

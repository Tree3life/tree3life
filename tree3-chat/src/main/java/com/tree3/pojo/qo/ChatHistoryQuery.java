package com.tree3.pojo.qo;

import com.tree3.pojo.entity.ChatHistory;
import lombok.*;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/29 11:17 </p>
 */
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
public class ChatHistoryQuery extends ChatHistory {
    private static final long serialVersionUID = -403162556125084568L;

    /**
     * 最新一条 退出消息的create_time
     */
    private Date logoutTime;
    /**
     * 退出消息的标记
     * {@link com.tree3.pojo.command.Command}-Logout(-2)
     */
    private Integer cmdLogout;

}

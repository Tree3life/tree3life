package com.tree3.service;

import com.tree3.session.impl.memory.Group;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 聊天组 接口
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 9:56 </p>
 */
public interface GroupChatService {

    //region 群管理

    /**
     * 创建一个聊天组, 如果不存在才能创建成功, 否则返回 null
     *
     * @param name    组名
     * @param members 成员
     * @return 成功时返回组对象, 失败返回 null
     */
    Group createGroup(String name, Set<String> members);

    /**
     * 编辑群信息
     */
    void editGroup();


    /**
     * 获取群信息
     */
    void getGroup();

    /**
     * 移除聊天组
     *
     * @param name 组名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    Group removeGroup(String name);
    //endregion 群管理

    //region 群成员管理

    /**
     * 群成员：加入聊天组
     *
     * @param name   组名
     * @param member 成员名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    Group joinMember(String name, String member);


    /**
     * 群主操作： 添加群成员
     */
    void addMember();

    /**
     * 群主操作： 添加一组群成员
     */
    void addMembers();

    /**
     * 群主操作：移除组成员
     *
     * @param name   组名
     * @param member 成员名
     * @return 如果组不存在返回 null, 否则返回组对象
     */
    Group removeMember(String name, String member);

    /**
     * 群主操作：批量移除组成员
     */
    void removeMembers();


    /**
     * 获取组成员
     *
     * @param name 组名
     * @return 成员集合, 没有成员会返回 empty set
     */
    Set<String> getMembers(String name);
    //endregion 群成员管理


    /**
     * 获取组成员的 channel 集合, 只有在线的 channel 才会返回
     *
     * @param name 组名
     * @return 成员 channel 集合
     */
    List<Channel> getMembersChannel(String name);


    /**
     * 发送群消息
     */
    boolean transferChatMessage(ChannelHandlerContext context, TextWebSocketFrame frame);
}

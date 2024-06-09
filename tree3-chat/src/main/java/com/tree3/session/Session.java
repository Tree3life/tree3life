package com.tree3.session;
import io.netty.channel.Channel;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 14:57 </p>
 */
public interface Session {

/**
 * 会话管理接口
 */
    /**
     * 绑定会话
     * @param channel 哪个 channel 要绑定会话
     * @param token 会话绑定用户
     */
    boolean bind(Channel channel, Integer userId);

    /**
     * 解绑会话
     * @param channel 哪个 channel 要解绑会话
     */
    void unbind(Channel channel);

    /**
     * 获取属性
     * @param channel 哪个 channel
     * @param name 属性名
     * @return 属性值
     */
    Object getAttribute(Channel channel, String name);

    /**
     * 设置属性
     * @param channel 哪个 channel
     * @param name 属性名
     * @param value 属性值
     */
    void setAttribute(Channel channel, String name, Object value);

    /**
     * 根据用户token获取 channel
     * @param token 用户token
     * @return channel
     */
    Channel getChannel(Integer userId);

     String showUserIdChannelMapInfo();
    /**
     * 根据用户ID获取 channel
     * @param userId 用户ID
     * @return channel
     */
//    Channel getChannel(int userId);
}

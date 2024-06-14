package com.tree3.session.impl;

import com.tree3.session.Session;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 15:38 </p>
 */
@Slf4j
@Scope(value = "singleton")
@Component
public class SessionImpl implements Session {
    private final Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();
    private final Map<Channel, Integer> channelUserIdMap = new ConcurrentHashMap<>();
    private final Map<Channel, Map<String, Object>> channelAttributesMap = new ConcurrentHashMap<>();


    @Override
    public boolean bind(Channel channel, Integer userId) {
        log.trace("要绑定的用户id:{},channel:{}", userId, channel);
        try {
            userIdChannelMap.put(userId, channel);
            channelUserIdMap.put(channel, userId);
            channelAttributesMap.put(channel, new ConcurrentHashMap<>());
            log.debug("用户:{},成功与channel:{}进行了绑定", userId, channel);
            return true;
        } catch (RuntimeException e) {
            log.error("聊天bind()异常：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public void unbind(Channel channel) {
        if (ObjectUtils.isEmpty(channel)) {
            log.error("空的channale{}", channel);
            return;
        }
        Integer userId = channelUserIdMap.remove(channel);
        if (!ObjectUtils.isEmpty(userId)) {
            userIdChannelMap.remove(userId);
        }
        channelAttributesMap.remove(channel);
    }


    @Override
    public Channel getChannel(Integer userId) {
        return userIdChannelMap.get(userId);
    }

    @Override
    public Integer getUserId(Channel channel) {
        return channelUserIdMap.get(channel);
    }

    @Override
    public Object getAttribute(Channel channel, String name) {
        return channelAttributesMap.get(channel).get(name);
    }

    @Override
    public void setAttribute(Channel channel, String name, Object value) {
        channelAttributesMap.get(channel).put(name, value);
    }

    @Override
    public String showUserIdChannelMapInfo() {
        return userIdChannelMap.toString();
    }
}

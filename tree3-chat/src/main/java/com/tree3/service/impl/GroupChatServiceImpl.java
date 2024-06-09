package com.tree3.service.impl;

import com.tree3.service.GroupChatService;
import com.tree3.session.Session;
import com.tree3.session.impl.memory.Group;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 聊天程序处理器
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 15:28 </p>
 */
@Component
public class GroupChatServiceImpl implements GroupChatService {


    /**
     *
     */
    @Resource
    private Session session;


    @Override
    public Group createGroup(String name, Set<String> members) {
        return null;
    }

    @Override
    public void editGroup() {

    }

    @Override
    public void getGroup() {

    }

    @Override
    public Group removeGroup(String name) {
        return null;
    }

    @Override
    public Group joinMember(String name, String member) {
        return null;
    }

    @Override
    public void addMember() {

    }

    @Override
    public void addMembers() {

    }

    @Override
    public Group removeMember(String name, String member) {
        return null;
    }

    @Override
    public void removeMembers() {

    }

    @Override
    public Set<String> getMembers(String name) {
        return null;
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
        return null;
    }

    @Override
    public boolean transferChatMessage(ChannelHandlerContext context, TextWebSocketFrame frame) {

        return false;
    }
}

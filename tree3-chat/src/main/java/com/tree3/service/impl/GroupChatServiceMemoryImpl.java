package com.tree3.service.impl;

import com.tree3.service.GroupChatService;
import com.tree3.session.impl.memory.Group;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GroupChatServiceMemoryImpl implements GroupChatService {
    private final Map<String, Group> groupMap = new ConcurrentHashMap<>();

    @Override
    public Group createGroup(String name, Set<String> members) {
        Group group = new Group(name, members);
        return groupMap.putIfAbsent(name, group);
    }

    @Override
    public void editGroup() {

    }

    @Override
    public void getGroup() {

    }

    @Override
    public Group joinMember(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().add(member);
            return value;
        });
    }

    @Override
    public void addMember() {

    }

    @Override
    public void addMembers() {

    }

    @Override
    public Group removeMember(String name, String member) {
        return groupMap.computeIfPresent(name, (key, value) -> {
            value.getMembers().remove(member);
            return value;
        });
    }

    @Override
    public void removeMembers() {

    }

    @Override
    public Group removeGroup(String name) {
        return groupMap.remove(name);
    }

    @Override
    public Set<String> getMembers(String name) {
        return groupMap.getOrDefault(name, Group.EMPTY_GROUP).getMembers();
    }

    @Override
    public List<Channel> getMembersChannel(String name) {
//        return getMembers(name).stream()
//                .map(member -> SessionFactory.getSession().getChannel(member))
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
        return null;
    }

    @Override
    public boolean transferChatMessage(ChannelHandlerContext context, TextWebSocketFrame frame) {
        return false;
    }

}

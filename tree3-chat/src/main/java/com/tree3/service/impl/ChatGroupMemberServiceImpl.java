package com.tree3.service.impl;

import com.tree3.pojo.entity.ChatGroupMember;
import com.tree3.dao.ChatGroupMemberMapper;
import com.tree3.service.ChatGroupMemberService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author rupert
 * @since 2024-05-21 08:54:21
 */
@Slf4j
@Service("chatGroupMemberService")
public class ChatGroupMemberServiceImpl implements ChatGroupMemberService {

    private final ChatGroupMemberMapper chatGroupMemberMapper;

    @Autowired
    public ChatGroupMemberServiceImpl(ChatGroupMemberMapper chatGroupMemberMapper) {
        this.chatGroupMemberMapper = chatGroupMemberMapper;
    }

    @Override
    public List<ChatGroupMember> queryAll() {
        return chatGroupMemberMapper.queryChatGroupMembers();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatGroupMember> queryAll(ChatGroupMember chatGroupMember, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return chatGroupMemberMapper.queryChatGroupMembers(chatGroupMember, offset, pageSize);
    }


    @Override
    public ChatGroupMember queryChatGroupMember(ChatGroupMember chatGroupMember) {
        return chatGroupMemberMapper.queryChatGroupMember(chatGroupMember);
    }


    @Override
    public ChatGroupMember queryById(Integer id) {
        return chatGroupMemberMapper.queryById(id);
    }

    @Override
    public ChatGroupMember update(ChatGroupMember chatGroupMember) {
        this.chatGroupMemberMapper.update(chatGroupMember);
        return queryById(chatGroupMember.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatGroupMemberMapper.deleteById(id) > 0;
    }

    @Override
    public ChatGroupMember insert(ChatGroupMember chatGroupMember) {
        this.chatGroupMemberMapper.insert(chatGroupMember);
        return chatGroupMember;
    }
}

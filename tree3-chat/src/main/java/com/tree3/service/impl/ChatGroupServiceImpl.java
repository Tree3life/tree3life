package com.tree3.service.impl;

import com.tree3.dao.ChatGroupMapper;
import com.tree3.pojo.dto.ChatGroupDTO;
import com.tree3.pojo.entity.ChatGroup;
import com.tree3.pojo.qo.ChatGroupVo;
import com.tree3.service.ChatGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rupert
 * @since 2024-05-20 16:19:30
 */
@Slf4j
@Service("chatGroupService")
public class ChatGroupServiceImpl implements ChatGroupService {

    private final ChatGroupMapper chatGroupMapper;

    @Autowired
    public ChatGroupServiceImpl(ChatGroupMapper chatGroupMapper) {
        this.chatGroupMapper = chatGroupMapper;
    }

    @Override
    public List<ChatGroup> queryAll() {
        return chatGroupMapper.queryChatGroups();
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatGroup> queryAll(ChatGroup chatGroup, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return chatGroupMapper.queryChatGroups(chatGroup, offset, pageSize);
    }


    @Override
    public ChatGroup queryChatGroup(ChatGroup chatGroup) {
        return chatGroupMapper.queryChatGroup(chatGroup);
    }

    @Override
    public List<ChatGroupDTO> queryChatGroupsWithHistory(ChatGroupVo chatGroup) {
        return chatGroupMapper.queryChatGroupPro(chatGroup);
    }

    @Override
    public List<ChatGroupDTO> queryChatGroupsWithLastMessage(ChatGroupVo chatGroup) {
        return chatGroupMapper.queryChatGroupWithLastMessage(chatGroup);
    }

    @Override
    public ChatGroup queryById(Integer id) {
        return chatGroupMapper.queryById(id);
    }

    @Override
    public ChatGroup update(ChatGroup chatGroup) {
        this.chatGroupMapper.update(chatGroup);
        return queryById(chatGroup.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatGroupMapper.deleteById(id) > 0;
    }

    @Override
    public ChatGroup insert(ChatGroup chatGroup) {
        this.chatGroupMapper.insert(chatGroup);
        return chatGroup;
    }
}

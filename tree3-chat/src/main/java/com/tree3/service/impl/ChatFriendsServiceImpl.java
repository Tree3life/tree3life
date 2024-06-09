package com.tree3.service.impl;

import com.tree3.dao.ChatFriendsMapper;
import com.tree3.pojo.dto.ChatFriendDTO;
import com.tree3.pojo.entity.ChatFriends;
import com.tree3.pojo.qo.ChatFriendsQuery;
import com.tree3.service.ChatFriendsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author rupert
 * @since 2024-05-20 16:19:51
 */
@Slf4j
@Service("chatFriendsService")
public class ChatFriendsServiceImpl implements ChatFriendsService {

    private final ChatFriendsMapper chatFriendsMapper;

    @Autowired
    public ChatFriendsServiceImpl(ChatFriendsMapper chatFriendsMapper) {
        this.chatFriendsMapper = chatFriendsMapper;
    }

    @Override
    public List<ChatFriends> queryAll() {
        return chatFriendsMapper.queryChatFriendss();
    }


//    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ChatFriends> queryAll(ChatFriends chatFriends, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        return chatFriendsMapper.queryChatFriendss(chatFriends, offset, pageSize);
    }
    @Override
    public List<ChatFriendDTO> queryChatFriendsPro(ChatFriendsQuery chatFriends) {
        return chatFriendsMapper.queryChatFriendsWidthLastMsg(chatFriends);
    }

    @Override
    public ChatFriends queryChatFriends(ChatFriends chatFriends) {
        return chatFriendsMapper.queryChatFriends(chatFriends);
    }


    @Override
    public ChatFriends queryById(Integer id) {
        return chatFriendsMapper.queryById(id);
    }

    @Override
    public ChatFriends update(ChatFriends chatFriends) {
        this.chatFriendsMapper.update(chatFriends);
        return queryById(chatFriends.getId());
    }

    @Override
    public boolean deleteById(Integer id) {
        return chatFriendsMapper.deleteById(id) > 0;
    }

    @Override
    public ChatFriends insert(ChatFriends chatFriends) {
        this.chatFriendsMapper.insert(chatFriends);
        return chatFriends;
    }
}

package com.tree3.service.impl;

import com.tree3.constants.RedisConstance;
import com.tree3.dao.ChatHistoryMapper;
import com.tree3.pojo.PojoConvertor;
import com.tree3.pojo.command.Command;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.message.Message;
import com.tree3.pojo.message.MessageText;
import com.tree3.service.PrivateChatService;
import com.tree3.session.Session;
import com.tree3.utils.JSONUtils;
import com.tree3.utils.RedisCache;
import com.tree3.utils.RedisUtils;
import com.tree3.utils.ResponseHelperWebSocket;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tree3.config.RabbitMQConsumerConfig.RabbitMQElements.TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 16:01 </p>
 */
@Slf4j
@Component
public class PrivateChatServiceImpl implements PrivateChatService {

    @Resource
    private Session session;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisCache redisCache;

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private ChatHistoryMapper historyMapper;


    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRED)
    @Override
    public List<ChatHistory> queryChatHistory(ChannelHandlerContext context, TextWebSocketFrame frame) {
        MessageText msg = JSONUtils.paresToObj(frame.text(), MessageText.class);
        log.debug("查询用户间的聊天记录:{}", msg);
        Integer friendId = Integer.valueOf(Objects.requireNonNull(msg.getContent()));
        int userId = msg.getFrom();

        //前端直接将未读消息的id传过来,更新两者 用户的未读记录,取消未读消息 (Rupert，2024/5/30 10:00)
        Object payload = msg.getPayload();
        if (payload != null) {
            ArrayList<Integer> readMsg = (ArrayList<Integer>) payload;
            if (!ObjectUtils.isEmpty(readMsg)) {
                boolean done = historyMapper.updateUnreadMessage(readMsg, Message.Status_Read);
                log.debug("查询并更新消息的状态：user:{}-->friend:{},msgId:{}}", msg.getFrom(), msg.getContent(), readMsg);
            }
        }

//        ChatHistoryQuery historyQuery = new ChatHistoryQuery();
//        historyQuery.setFrom(friendId);
//        historyQuery.setTo(userId);
//        historyQuery.setCommand(Command.PrivateChatText.getType());
//        historyQuery.setCmdLogout(Command.Logout.getType());
//        historyMapper.updateUnreadMessage(historyQuery);

        // optimized：查询两者的聊天记录，配合前端的滚动条进行分页优化 (Rupert，2024/5/30 )
        List<ChatHistory> chatHistories = historyMapper.queryFriendsChatHistory(userId, friendId, Command.PrivateChatText.getType());
        context.writeAndFlush(ResponseHelperWebSocket.success(Command.ResponsePrivateQueryHistory, userId, chatHistories));
        return chatHistories;
    }


    //{"createTime":"2024-05-24T00:39:10.953Z","commandType":11,"state":-1,"from":1,"to":2,"content":"aaa","id":null}
    @Override
    public void transferChatMessage(ChannelHandlerContext context, TextWebSocketFrame frame) {
        //Step 1: 接收单聊消息
        MessageText msg = JSONUtils.paresToObj(frame.text(), MessageText.class);
        Integer from = msg.getFrom();
        Integer to = msg.getTo();
        String content = msg.getContent();
        //修改当前消息的状态为已送达服务器&未读
        msg.setState(Message.Status_Delivered);

        //Step 3: 获取收件人对应的channel
        Channel toChannel = session.getChannel(to);


        //Step 4: 判断用户是否在线
        if (!ObjectUtils.isEmpty(toChannel)) {
            //4.1收件人在线，向收件人进行转发
            toChannel.writeAndFlush(ResponseHelperWebSocket.privateMessage(msg));
            log.debug("已转发私聊消息 from:{}->to:{}      {}", from, to, msg);
        } else {//4.2不在线，暂存消息，发送消息
            // optimized：离线消息： 将离线消息存储到redis 的 一个 离线map结构中 (Rupert，2024/5/23 )
            List<ChatHistory> userHistory = redisCache.getCacheMapValue(RedisConstance.CHAT_HISTORY, msg.getTo().toString());
            if (CollectionUtils.isEmpty(userHistory)) {
                userHistory = new ArrayList<>(1);
            }

            ChatHistory privateMsg = PojoConvertor.convertTOChatHistory(msg);
            userHistory.add(privateMsg);

            redisCache.setCacheMapValue(RedisConstance.CHAT_HISTORY, msg.getTo().toString(), userHistory);
            //{user1Id:[离线消息(set)],user2Id:[离线消息(set)]}
//                rabbitTemplate.convertAndSend("", RabbitMQConsumerConfig.TREE3CHAT_QUEUE_OFFLINE$MESSAGE_WORK, msg, (message) -> {
//                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                    return message;
//                });
        }

        //Step 5: 将消息发送到 消息队列中 ，后续进行数据库存储（利用MQ 进行异步削峰）
        rabbitTemplate.convertAndSend("", TREE3CHAT_QUEUE_SAVE$CHAT$HISTORY_WORK, msg, new CorrelationData(redisUtils.nextDistributedID(RedisConstance.RabbitMQ_MSGID) + "")
//                , message -> {
//                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
//                    return message;
//                }
        );
    }

    @Override
    public void sendMessage() {
        System.out.println("xxxxxxx");
    }

}

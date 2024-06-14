package com.tree3.service.impl;

import com.tree3.constants.RedisConstance;
import com.tree3.dao.ChatHistoryMapper;
import com.tree3.exception.BusinessException;
import com.tree3.pojo.PojoConvertor;
import com.tree3.pojo.command.Command;
import com.tree3.pojo.dto.ChatFriendDTO;
import com.tree3.pojo.dto.ChatGroupDTO;
import com.tree3.pojo.entity.ChatHistory;
import com.tree3.pojo.entity.User;
import com.tree3.pojo.message.Message;
import com.tree3.pojo.message.MessageText;
import com.tree3.pojo.qo.ChatFriendsQuery;
import com.tree3.pojo.qo.ChatGroupVo;
import com.tree3.service.AuthService;
import com.tree3.service.ChatFriendsService;
import com.tree3.service.ChatGroupService;
import com.tree3.service.ChatHistoryService;
import com.tree3.session.Session;
import com.tree3.utils.JSONUtils;
import com.tree3.utils.RedisCache;
import com.tree3.utils.ResponseHelperWebSocket;
import com.tree3.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.tree3.constants.CommunicationMessage.ILLEGAL_TOKEN;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/8 16:01 </p>
 */
@Slf4j
@Component
public class AuthServiceImpl implements AuthService {

    @Resource
    private ChatHistoryMapper historyMapper;
    /**
     * 会话管理
     */
    @Resource
    private Session session;

    @Override
    public boolean login(ChannelHandlerContext context, TextWebSocketFrame frame) {
        Channel currentChannel = context.channel();
        MessageText msg = JSONUtils.paresToObj(frame.text(), MessageText.class);
        log.trace("进行用户id和channel的绑定，channel:{},msg:{}：", currentChannel, msg);
        RedisCache redisCache = SpringUtil.getBean(RedisCache.class);
        ChatFriendsService friendsService = SpringUtil.getBean(ChatFriendsService.class);
        ChatGroupService chatGroupService = SpringUtil.getBean(ChatGroupService.class);
        ChatHistoryService historyService = SpringUtil.getBean(ChatHistoryService.class);
        Session session = SpringUtil.getBean(Session.class);

        //Step 1: 根据token获取用户id
        String tokenSuffix = msg.getContent();
        log.debug("tokenSuffix:", tokenSuffix);
        if (StringUtils.isEmpty(tokenSuffix)) {
            // 一个channel就是一个连接 (Rupert，2024/5/26 9:52)
            //Step 1.1: 向客户端写出 'token无效'
            //将异常信息返回给前端
            currentChannel.writeAndFlush(ResponseHelperWebSocket.fail(ILLEGAL_TOKEN));

            //Step 1.2: 关闭与客户端的连接
//            context.fireChannelInactive();
            currentChannel.close();
            return false;
        }
        String tokenKey = RedisConstance.TOKEN_KEY + tokenSuffix;

        User user = redisCache.getCacheObject(tokenKey);
        if (ObjectUtils.isEmpty(user)) {
            log.warn("收到的token:{}", tokenKey);
            throw new BusinessException("未能通过token获取到正确的用户信息");
        }

        Integer userId = user.getId();

        //Step 2: 将channel与用户id进行绑定
        boolean bind = session.bind(currentChannel, userId);

        //绑定成功
        if (bind) {
            //todo 离线消息： 1.查询redis中存储的离线消息map,

            //Step 3: 查找用户的 朋友、群聊、及历史记录等信息
            //todo 返回 聊天系统 的 各种信息
            // {
            //    分类信息：（朋友、群聊）
            //    朋友:{基本消息、聊天记录}
            //    群聊：{群信息、群聊聊天记录}
            //todo 离线消息： 2.遍历拿到的离线消息将离线消息（messageType区分聊天类型） 分发到各个好友/群聊的聊天记录集合里），
            // }
            //todo 好友和群聊消息： 应当存入到redis中，此处先从redis中取，没有的话再从mysql中取
            log.debug("friendsService:{}", friendsService);
            log.debug("chatGroupService:{}", chatGroupService);
            log.debug("historyService:{}", historyService);
            ChatFriendsQuery queryFriends = new ChatFriendsQuery();
            queryFriends.setUserId(userId);
            queryFriends.setMsgState(Message.Status_Delivered);
            queryFriends.setCmd(Command.PrivateChatText.getType());//查询 私聊的消息
            queryFriends.setCmdLogout(Command.Logout.getType());
//            ChatFriendDTO friends = friendsService.queryChatFriends(queryFriends);
            List<ChatFriendDTO> friends = friendsService.queryChatFriendsPro(queryFriends);

            //查询该用户所属的群聊信息
            ChatGroupVo queryChat = new ChatGroupVo();
            queryChat.setUserId(userId);
            queryChat.setCommand(Command.GroupChatText.getType());
            List<ChatGroupDTO> chatGroup = chatGroupService.queryChatGroupsWithLastMessage(queryChat);

            //响应请求
            HashMap<String, Object> result = new HashMap<>();
            result.put("friends", friends);
            result.put("groups", chatGroup);
            currentChannel.writeAndFlush(ResponseHelperWebSocket.success(Command.ResponseLogin, userId, result));
        }
        context.fireChannelRead(msg);
        return true;
    }

    @Override
    public boolean logout(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        log.warn("处理退出 聊天服务器的请求。。。。{}", frame);
        //接收一条下线的消息记录，用于实现该用户的 群聊未读消息 的记录 考虑用Command.Offline或Command.Logout实现
        MessageText message = JSONUtils.paresToObj(frame.text(), MessageText.class);
        message.setState(Message.Status_Delivered);

        //Step 1: 将消息 转换为 chathistory 并存储(操作在 {@link com.tree3.handler.channel.Tree3ChatHandler }channelInactive()或exceptionCaught进行)
        ChatHistory chatHistory = PojoConvertor.convertTOChatHistory(message);
        historyMapper.insert(chatHistory);

        /**
         *  Step 2:解除session 绑定 操作在 {@link com.tree3.handler.channel.Tree3ChatHandler }channelInactive()或exceptionCaught进行
         *  session.unbind(ctx.channel());
         */
        session.unbind(ctx.channel());
        //断开连接
        ctx.channel().close();
        return false;
    }

    @Override
    public void handlePing(ChannelHandlerContext context, TextWebSocketFrame frame) {
        MessageText msg = JSONUtils.paresToObj(frame.text(), MessageText.class);
        // 1.向session的中对应的channel中记录本次Ping的时间（供消息发送时做参考，若长时间未收到Ping消息，则认为客户端已关闭，走离线消息逻辑）
        session.setAttribute(context.channel(), "lastPingTime", LocalDateTime.now());
        // {"id":"2-XEtu","createTime":"2024-06-13T13:43:09.012Z","commandType":3,"from":1}
        // 向客户端写回Pong消息，客户端需要维护一个 接收Pong消息的字段 lastReciveTime
        context.channel().writeAndFlush(ResponseHelperWebSocket.success(Command.Pong, msg.getFrom(), ""));
        log.debug("收到的ping消息:" + frame);
    }

    @Override
    public void handlePong(ChannelHandlerContext context, TextWebSocketFrame frame) {
        System.out.println("pong:" + frame);
    }
}

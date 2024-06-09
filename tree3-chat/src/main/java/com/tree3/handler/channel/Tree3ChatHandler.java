package com.tree3.handler.channel;

import com.tree3.service.chat.GroupChatDispatcher;
import com.tree3.service.chat.PrivateChatDispatcher;
import com.tree3.utils.ResponseHelperWebSocket;
import com.tree3.service.chat.AuthDispatcher;
import com.tree3.pojo.command.Command;
import com.tree3.pojo.message.MessageHead;
import com.tree3.session.Session;
import com.tree3.utils.JSONUtils;
import com.tree3.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * <p>
 * websocket聊天消息处理
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 10:17 </p>
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class Tree3ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /**
     * 客户端连接的时候触发
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //todo 如果 websocket在建立连接时能够 传递token，在此处进行 channel与session的绑定
        log.info("111111新客户端连接：{}，当前channel{}", channel.remoteAddress(), channel);
        showUserBindChannel("handlerAdded");
        super.channelActive(ctx);
    }


    /**
     * 分发处理各类消息任务
     *
     * @param context
     * @param frame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, TextWebSocketFrame frame) throws Exception {
        Channel channel = context.channel();
        log.info("444444读事件----当前channel{}", channel);
        Assert.notNull(frame, "接收到的websocket消息为null");
        String wsMsg = frame.text();
        log.info("收到的原生消息为：{}", wsMsg);
        //Step 1: 获取消息对象
        MessageHead msgHead = JSONUtils.paresToObj(wsMsg, MessageHead.class);
        /**
         * 针对不同的 "业务" 命令 ，做处理
         */
        int cmdType = msgHead.getCommandType();
        Command command = Command.roughlyMatch(cmdType);
        log.debug(wsMsg);
        switch (command) {
            case SessionCorrelation://连接、断开、认证 等相关
                Command match = Command.match(cmdType);
                AuthDispatcher authDispatcher = SpringUtil.getBean(AuthDispatcher.class);
                authDispatcher.dispatch(match, context, frame);
                showUserBindChannel("SessionCorrelation");
                break;

            case PrivateChatCorrelation://私聊相关
                PrivateChatDispatcher privateChatDispatcher = SpringUtil.getBean(PrivateChatDispatcher.class);
                privateChatDispatcher.dispatch(Command.match(cmdType), context, frame);
                break;

            case GroupChatCorrelation://群聊相关
                GroupChatDispatcher groupChatDispatcher = SpringUtil.getBean(GroupChatDispatcher.class);
                groupChatDispatcher.dispatch(Command.match(cmdType), context, frame);
                break;

            default:
                log.error("未知的命令类型。。。。。。。{}", msgHead);
                break;
        }
    }

    /**
     * 主动关闭连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //从session中解除用户和channel的绑定
        Session session = SpringUtil.getBean(Session.class);
        showUserBindChannel("555555handlerRemoved-》前");
        session.unbind(ctx.channel());
        showUserBindChannel("handlerRemoved-》后");
        // unsure：如果是异常断开，根据用户的channel获取其对应的id，是否向数据库保存一条 该用户已离线的消息(配合实现未读消息) (Rupert，2024/5/29 20:01)
        log.info("channelInactive客户端退出了,成功解除客户端:{}与channel:{}的绑定", ctx.channel().remoteAddress(), ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常栈跟踪
        log.error("666666打印异常栈跟踪：{}", cause.getMessage());
        //将异常信息返回给前端
        ctx.writeAndFlush(ResponseHelperWebSocket.fail(cause.getMessage()));
        // debug@Rupert：
        cause.printStackTrace();

        //  发生异常时Channel是否应当被关闭
        //  （猜测：不应当被关闭，一个channel就是 `一个用户的某次连接`，即一个channel仅与一个用户有关，
        // unsure：SpringUtil.getBean(Session.class).unbind(ctx.channel()); (Rupert，2024/5/28 8:05)
        // unsure：出现异常后 应当【取消与客户端的连接】 context.close 还是 channel.close() (Rupert，2024/5/28 8:05)
        ctx.channel().close();
//        ctx.close();

    }


    private void showUserBindChannel(String mark) {
        Session session = SpringUtil.getBean(Session.class);
        String s = session.showUserIdChannelMapInfo();
        System.out.println(mark + "----》" + s);
    }
}

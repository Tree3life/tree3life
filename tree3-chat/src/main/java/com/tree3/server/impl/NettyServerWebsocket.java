package com.tree3.server.impl;

import com.tree3.exception.BusinessException;
import com.tree3.handler.channel.Tree3ChatHandler;
import com.tree3.server.NettyServer;
import com.tree3.session.Session;
import com.tree3.utils.SpringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 9:03 </p>
 */
@Slf4j
@Component
public class NettyServerWebsocket implements NettyServer {
    /**
     * 空闲检测的间隔事件设置为： 5分钟（300秒 ）
     * 前端心跳包的发送间隔设置为：2分钟（120秒 ）
     */
    private static final int idleDetectionInterval = 300;
    @Autowired
    private Tree3ChatHandler tree3ChatHandler;

    /**
     * 启动websocket服务
     *
     * @param wsPort 端口号
     * @return
     */
    @Override
    public ChannelFuture start(Integer wsPort) {
        //NioEventLoop默认个数=max(1,netty系统参数配置,cpu核心数*2)
        NioEventLoopGroup bossEventLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerEventLoopGroup = new NioEventLoopGroup(1);
        //用于处理耗时的任务，解放bossEventLoopGroup或workerEventLoopGroup
        DefaultEventLoopGroup longTaskProcessing = new DefaultEventLoopGroup();
        //负责组装 服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap()
                //Step 1: 配置EventLoopGroup，用于处理channel上发生的 源源不断的各种事件
                //boss只负责ServerSocketChannel上accept事件；worker只负责socketChannel上的读写
                .group(bossEventLoopGroup, workerEventLoopGroup)
                //Step 2: 设置`服务器socket`的 具体实现类型
                .channel(NioServerSocketChannel.class)
                //region Step 3:  通信管道 的初始化 配置
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    //在 管道初始化时 被调用
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        /**
                         * 在 管道初始化时 添加各种 信息处理器
                         * ChannelHandlerAdapter
                         * ChannelInboundHandlerAdapter 消息入站时 发挥作用的处理器
                         * ChannelOutboundHandlerAdapter 消息出站时 发挥作用的处理器
                         */
                        //将字节数据 转换为 字符串数据
//                        pipeline.addLast("字符串解析", new StringDecoder());

                        //region websocket 协议配置
                        pipeline
                                .addLast(new HttpServerCodec())
                                .addLast(new ChunkedWriteHandler())
                                .addLast(new HttpObjectAggregator(1024 * 64))
                                // 运行一个websocket服务器，负责websocket的握手以及对控制帧的处理，ping pong，文本或二进制都会传给下一个处理器来处理。
                                // 参数表示websocket的网址，通常形式：ws://server:port/context_path ，访问本机：ws://localhost:8899/hello
                                .addLast(new WebSocketServerProtocolHandler("/tree3chat"));
                        // 在10秒之内收不到消息自动断开
//                                .addLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS));
//                                .addLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS));
                        //endregion websocket 协议配置

                        //region 心跳处理 IdleStateHandler 是netty 提供的处理空闲状态的处理器
                        // 用来判断是不是 读空闲时间过长，或 写空闲时间过长  ;
                        //long readerIdleTime : 表示多长时间没有读, 就会发送一个心跳检测包检测是否连接;
                        //long writerIdleTime : 表示多长时间没有写, 就会发送一个心跳检测包检测是否连接
                        //long allIdleTime : 表示多长时间没有读写, 就会发送一个心跳检测包检测是否连接
                        // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件,当 IdleStateEvent 触发后 , 就会传递给管道 的下一个handler去处理
                        // 通过调用(触发)下一个handler 的 userEventTiggered , 在该方法中去处理 IdleStateEvent(读空闲，写空闲，读写空闲)
                        pipeline.addLast("空闲检测", new IdleStateHandler(idleDetectionInterval, idleDetectionInterval, idleDetectionInterval, TimeUnit.SECONDS));
//                        // ChannelDuplexHandler 可以同时作为入站和出站处理器
                        pipeline.addLast("处理检测到的空闲事件", new ChannelDuplexHandler() {
                            // 心跳检测逻辑(Rupert，2024/6/13 8:37)
                            //  1.客户端发送Ping，服务器接收到Ping之后，响应一个Pong,并记录 接收到的最新消息的的时间戳，保存到session中去
                            //  1.2 客户端每次发送消息之前进行检查，若 ((now-lastReceiveMsgTime)>心跳间隔的2.5倍？连接已断开：记录最新的心跳时间戳)则认为 服务器异常，报错/连接重试
                            //  2.当服务器触发读空闲之后，直接关闭连接;(连接的保活 应当交给客户端进行负责，减轻服务器压力)
                            //  3.服务记录每个客户端的最新的ping消息时间戳；每次发送消息之前 检查 （now-收到的 客户端的发来的最新消息的时间戳>心跳间隔的2.5倍?连接已断开，走离线消息处理流程:连接正常，正常转发消息）

                            /**
                             * 处理检测到的空闲事件
                             * @param ctx
                             * @param evt
                             * @throws Exception
                             */
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

                                if (evt instanceof IdleStateEvent) {
                                    IdleStateEvent event = (IdleStateEvent) evt;
                                    // 处理读空闲事件
                                    Channel channel = ctx.channel();
                                    if (event.state() == IdleState.READER_IDLE) {
                                        //移除session中对应的channel
                                        Session session = SpringUtil.getBean(Session.class);
                                        session.unbind(channel);
                                        channel.close();//断开连接
                                        log.warn("检测到{}：已经 {}s 没有读到数据了,已关闭连接", channel.id(), idleDetectionInterval);
                                    }
                                }
                            }
                        });
                        //endregion 心跳处理

//region 业务处理器
                        //聊天处理器,由于 之前的处理器 已经将信息完整的拿到，所以本处理器是 Sharable的
                        pipeline.addLast(tree3ChatHandler);

//endregion 业务处理器
                    }
                });
        //endregion Step 3: 配置通信管道

        try {
            //绑定监听端口
            return serverBootstrap.bind(wsPort)
                    .sync();//同步阻塞等待启动成功
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new BusinessException("启动websocket 服务器失败。。。");
        }
    }

    @Override
    public boolean destroy() {
        System.out.println("销毁websocket服务");
        return false;
    }

    private static void debug(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf.toString());
    }
}

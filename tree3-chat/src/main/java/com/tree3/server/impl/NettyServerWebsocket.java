package com.tree3.server.impl;

import com.tree3.exception.BusinessException;
import com.tree3.handler.channel.Tree3ChatHandler;
import com.tree3.server.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
                                .addLast(new WebSocketServerProtocolHandler("/tree3chat"));
                        //endregion websocket 协议配置

                        //region 心跳处理
//                        // 用来判断是不是 读空闲时间过长，或 写空闲时间过长
//                        // 5s 内如果没有收到 channel 的数据，会触发一个 IdleState#READER_IDLE 事件
//                        ch.pipeline().addLast(new IdleStateHandler(5, 0, 0));
//                        // ChannelDuplexHandler 可以同时作为入站和出站处理器
//                        ch.pipeline().addLast(new ChannelDuplexHandler() {
//                            // 用来触发特殊事件
//                            @Override
//                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//                                IdleStateEvent event = (IdleStateEvent) evt;
//                                // 触发了读空闲事件
//                                if (event.state() == IdleState.READER_IDLE) {
//                                    log.debug("已经 5s 没有读到数据了");
//                                    ctx.channel().close();
//                                }
//                            }
//                        });
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

package com.tree3.server;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.net.NetUtil;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Netty启动类
 */
@Component
@Slf4j
public class NettyStartServer implements ApplicationRunner {

    @Resource
    private NettyServer nettyServer;

    @Value("${netty.ws.port}")
    private Integer wsPort;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 新开线程启动netty服务，不然会阻塞springboot主进程
        new Thread(this::startNetty).start();
        // 关闭监听
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            nettyServer.destroy();
        }));
    }

    private void startNetty() {
        log.info("The Netty Server for websocket is beginning to start...[{}]", wsPort);
        try {
            // 启动netty服务
            ChannelFuture future = nettyServer.start(wsPort);

            String hostAddress = NetUtil.getLocalhost().getHostAddress();
            log.info("websocket服务启动成功...{}:{}", hostAddress, wsPort);
//            log.info("websocket服务已注册到nacos服务中...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Netty Server start error: {}", ExceptionUtil.stacktraceToString(e));
        }
    }
}
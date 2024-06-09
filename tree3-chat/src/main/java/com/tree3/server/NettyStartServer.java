package com.tree3.server;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.net.NetUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import io.netty.channel.ChannelFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Netty启动类
 */
@Component
@Slf4j
public class NettyStartServer implements ApplicationRunner {

    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Value("${netty.ws.port}")
    private Integer wsPort;
    @Value("${spring.application.name}")
    private String serverName;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 需要新开线程启动netty服务，不然会阻塞springboot主进程
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
            log.info("websocket服务启动成功.....");

            // 将netty服务注册到nacos
            Properties properties = new Properties();
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDiscoveryProperties.getServerAddr());
            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosDiscoveryProperties.getNamespace());
            properties.setProperty(PropertyKeyConst.USERNAME, nacosDiscoveryProperties.getUsername());
            properties.setProperty(PropertyKeyConst.PASSWORD, nacosDiscoveryProperties.getPassword());
            NamingService namingService = NamingFactory.createNamingService(properties);
            String hostAddress = NetUtil.getLocalhost().getHostAddress();
            namingService.registerInstance(serverName, hostAddress, wsPort);
            log.info("websocket服务地址.....[{}:{}]", hostAddress, wsPort);

            log.info("websocket服务已注册到nacos服务中...");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("Netty Server start error: {}", ExceptionUtil.stacktraceToString(e));
        }
    }
}
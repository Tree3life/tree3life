package com.tree3.server;

import io.netty.channel.ChannelFuture;

/**
 * <p>
 *
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/5/7 9:01 </p>
 */
public interface NettyServer {
    /**
     * 启动NettyServer
     * @return
     * @param wsPort
     */
    ChannelFuture start(Integer wsPort);

    /**
     * 销毁NettyServer
     * @return
     */
    boolean destroy();

}

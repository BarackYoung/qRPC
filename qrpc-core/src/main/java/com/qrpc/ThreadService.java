package com.qrpc;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/6
 **/
public class ThreadService {
    private volatile static EventLoopGroup serverWorkEventLoopGroup;
    private volatile static EventLoopGroup clientEventLoopGroup;
    private volatile static EventLoopGroup serverBossEventLoopGroup;
    private static final int processors;
    private static final int clientThreadNum;
    private static final int serverBossThreadNum;
    private static final int serverWorkThreadNum;

    static {
        processors = Runtime.getRuntime().availableProcessors();
        clientThreadNum = Math.max(Math.max(1, processors/4), 4);
        serverBossThreadNum = Math.max(Math.max(1, processors/4), 4);
        serverWorkThreadNum = Math.min(3, processors * 2);
    }

    public static EventLoopGroup getServerBossEventLoopGroup() {
        if (serverBossEventLoopGroup != null && !serverBossEventLoopGroup.isShutdown()
                && !serverBossEventLoopGroup.isTerminated()) {
            return serverBossEventLoopGroup;
        }
        synchronized (ThreadService.class) {
            if (serverBossEventLoopGroup != null && !serverBossEventLoopGroup.isShutdown()
                    && !serverBossEventLoopGroup.isTerminated()) {
                return serverBossEventLoopGroup;
            }
            serverBossEventLoopGroup = new NioEventLoopGroup(serverBossThreadNum);
            return serverBossEventLoopGroup;
        }
    }

    public static EventLoopGroup getServerWorkEventLoopGroup() {
        if (serverWorkEventLoopGroup != null && !serverWorkEventLoopGroup.isTerminated()
                && !serverWorkEventLoopGroup.isShutdown()) {
            return serverWorkEventLoopGroup;
        }
        synchronized (ThreadService.class) {
            if (serverWorkEventLoopGroup != null && !serverWorkEventLoopGroup.isTerminated()
                    && !serverWorkEventLoopGroup.isShutdown()) {
                return serverWorkEventLoopGroup;
            }
            serverWorkEventLoopGroup = new NioEventLoopGroup(serverWorkThreadNum);
            return serverWorkEventLoopGroup;
        }
    }

    public static EventLoopGroup getClientEventLoopGroup() {
        if (clientEventLoopGroup != null && !clientEventLoopGroup.isShutdown()
                && !clientEventLoopGroup.isTerminated()) {
            return clientEventLoopGroup;
        }
        synchronized (ThreadService.class) {
            if (clientEventLoopGroup != null && !clientEventLoopGroup.isShutdown()
                    && !clientEventLoopGroup.isTerminated()) {
                return clientEventLoopGroup;
            }
            clientEventLoopGroup = new NioEventLoopGroup(clientThreadNum);
            return clientEventLoopGroup;
        }
    }
}

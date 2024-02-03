package org.qrpc;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.*;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/6
 **/
public class ThreadService {
    private volatile static EventLoopGroup serverWorkEventLoopGroup;
    private volatile static EventLoopGroup clientEventLoopGroup;
    private volatile static EventLoopGroup serverBossEventLoopGroup;
    private volatile static ExecutorService businessExecutor;
    private static final int processors;
    private static final int clientThreadNum;
    private static final int serverBossThreadNum;
    private static final int serverWorkThreadNum;
    private static final int coreThread;
    private static final int maxThread;
    private static final BlockingQueue<Runnable> workQueue;

    static {
        processors = Runtime.getRuntime().availableProcessors();
        clientThreadNum = Math.min(Math.max(1, processors/4), 4);
        serverBossThreadNum = Math.max(processors/4, 1);
        serverWorkThreadNum = Math.max(6, processors * 3);
        coreThread = processors * 3;
        maxThread = processors * 10;
        workQueue = new LinkedBlockingQueue<>();
    }

    /**
     * 获取Reactor线程组
     *
     * @return BossEventLoopGroup
     */
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

    /**
     * 获取工作IO线程组
     *
     * @return ServerWorkEventLoopGroup
     */
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

    /**
     * 获取客户端线程组
     *
     * @return ClientEventLoopGroup
     */
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

    /**
     * 获取业务线程池
     *
     * @return BusinessExecutor
     */
    public static ExecutorService getBusinessExecutor () {
        if (businessExecutor != null && !businessExecutor.isShutdown() && !businessExecutor.isTerminated()) {
            return businessExecutor;
        }
        synchronized (ThreadService.class) {
            if (businessExecutor != null && !businessExecutor.isShutdown() && !businessExecutor.isTerminated()) {
                return businessExecutor;
            }
            businessExecutor = new ThreadPoolExecutor(coreThread, maxThread, 30, TimeUnit.SECONDS, workQueue);
            return businessExecutor;
        }
    }
}

package com.qrpc.server;

import com.qrpc.Banner;
import com.qrpc.Meta;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;

import java.net.InetSocketAddress;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/1
 **/
public class TCPServer implements Server {

    private static final int DEFAULT_BOSS_THREAD = 1;

    @Override
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(DEFAULT_BOSS_THREAD);
        int processorNum = Runtime.getRuntime().availableProcessors();
        int workThread = Math.min(3, processorNum * 2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workThread);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .localAddress(new InetSocketAddress(port))
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new ProtobufVarint32FrameDecoder())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new ProtobufDecoder(Meta.RpcMetaData.getDefaultInstance()))
                                    .addLast(new ProtobufInboundHandler());
                        }
                    });
            Banner.print();
            try {
                ChannelFuture future = bootstrap.bind().sync();
                future.addListener((ChannelFutureListener) channelFuture -> Banner.printStarted(port));
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}

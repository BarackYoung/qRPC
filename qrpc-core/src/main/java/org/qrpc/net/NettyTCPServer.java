package org.qrpc.net;

import org.qrpc.Banner;
import org.qrpc.Meta;
import org.qrpc.ThreadService;
import org.qrpc.server.AbstractServer;
import org.qrpc.server.ServerBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/1
 **/
@Slf4j
public class NettyTCPServer extends AbstractServer {

    private final int port;

    public NettyTCPServer(int port) {
        this.port = port;
    }

    public NettyTCPServer(ServerBuilder builder) {
        this.port = builder.getPort();
    }

    @Override
    public void start() {
        EventLoopGroup bossGroup = ThreadService.getServerBossEventLoopGroup();
        EventLoopGroup workerGroup = ThreadService.getServerWorkEventLoopGroup();
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
                                    .addLast(new ProtobufDecoder(Meta.RpcMetaData.getDefaultInstance()))
                                    .addLast(new ProtobufVarint32LengthFieldPrepender())
                                    .addLast(new ProtobufEncoder())
                                    .addLast(new NettyServerInboundHandler(NettyTCPServer.this));
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

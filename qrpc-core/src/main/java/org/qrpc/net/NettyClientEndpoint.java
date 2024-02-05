package org.qrpc.net;

import org.qrpc.Meta;
import org.qrpc.ThreadService;
import org.qrpc.client.Endpoint;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
@Slf4j
public class NettyClientEndpoint extends Endpoint {
    private final String ip;
    private final int port;
    private final EventLoopGroup eventLoopGroup;
    private ChannelFuture future;
    private final ChannelInboundHandler inboundHandler;


    public NettyClientEndpoint(String ip, int port) {
        super();
        this.ip = ip;
        this.port = port;
        this.eventLoopGroup = ThreadService.getClientEventLoopGroup();
        this.inboundHandler = new NettyClientInboundHandler(this);
        init();
    }

    private void init() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ProtobufVarint32FrameDecoder())
                                .addLast(new ProtobufDecoder(Meta.RpcMetaData.getDefaultInstance()))
                                .addLast(new ProtobufVarint32LengthFieldPrepender())
                                .addLast(new ProtobufEncoder())
                                .addLast(inboundHandler);
                    }
                });
        try {
            future = bootstrap.connect(ip, port).sync();
            future.addListener((GenericFutureListener<ChannelFuture>) future1 -> {
                if (!future1.isSuccess()) {
                    log.error("connect to {}:{} failed retrying", ip, port);
                    init();
                    return;
                }
                log.info("connected to {}:{}", ip, port);
                setReady(true);
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void send(Object o) {
        future.channel().writeAndFlush(o);
    }
}

package com.qrpc.client;

import com.google.protobuf.Message;
import com.google.protobuf.RpcController;
import com.qrpc.Meta;
import com.qrpc.ThreadService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
@Slf4j
public class NioQRpcClientEndpointEndpoint implements QRpcClientEndpoint<Message, Message> {
    private final String ip;
    private final int port;
    private final EventLoopGroup eventLoopGroup;
    private ChannelFuture future;

    public NioQRpcClientEndpointEndpoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.eventLoopGroup = ThreadService.getClientEventLoopGroup();
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
                                .addLast(new ProtobufEncoder())
                                .addLast(new ProtobufDecoder(Meta.RpcMetaData.getDefaultInstance()));
                    }
                });
    }

    @Override
    public void send(Message message, ResponseCallback<Message> responseCallback, RpcController rpcController) {

    }
}

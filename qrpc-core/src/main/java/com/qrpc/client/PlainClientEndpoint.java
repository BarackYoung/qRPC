package com.qrpc.client;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.qrpc.Meta;
import com.qrpc.MethodInfoHolder;
import com.qrpc.ThreadService;
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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
@Slf4j
public class PlainClientEndpoint implements QRpcClientEndpoint {
    private final String ip;
    private final int port;
    private final EventLoopGroup eventLoopGroup;
    private ChannelFuture future;
    private boolean initialed = false;
    private final ChannelInboundHandler inboundHandler;
    private final RequestCache requestCache;
    private final RequestFuture requestFuture;
    public PlainClientEndpoint(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.eventLoopGroup = ThreadService.getClientEventLoopGroup();
        this.requestCache = new RequestCache();
        this.requestFuture = new RequestFuture();
        this.inboundHandler = new DefaultChannelInboundHandler(this.requestCache, this.requestFuture);
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
                    System.out.println("connect failed，retrying...");
                    init();
                    return;
                }
                initialed = true;
            });
        } catch (InterruptedException e) {
            log.error("InterruptedException occurred");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    @Override
    public void send(MethodInfoHolder methodInfo) {
        if (!initialed) {
            methodInfo.getController().setFailed("connection is not initialed success.");
            return;
        }
        if (!future.channel().isActive()) {
            init();
        }
        Meta.RpcMetaData metaData = getMetaData(methodInfo);
        requestCache.register(metaData.getRequestId(), methodInfo);
        future.channel().writeAndFlush(metaData);
    }

    @Override
    public Message blockSend(MethodInfoHolder methodInfo) {
        if (!initialed) {
            methodInfo.getController().setFailed("connection is not initialed success.");
            return null;
        }
        Meta.RpcMetaData metaData = getMetaData(methodInfo);
        CompletableFuture<Message> responseFuture = new CompletableFuture<>();
        requestCache.register(metaData.getRequestId(), methodInfo);
        requestFuture.register(metaData.getRequestId(), responseFuture);
        future.channel().writeAndFlush(metaData);
        try {
            return responseFuture.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("InterruptedException occur");
            e.printStackTrace();
        } catch (ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean available() {
        return future.channel().isActive();
    }

    private Meta.RpcMetaData getMetaData(MethodInfoHolder methodInfo) {
        Descriptors.MethodDescriptor mDescriptor = methodInfo.getMethod();
        return Meta.RpcMetaData.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .setServiceName(mDescriptor.getService().getFullName())
                .setMethodId(mDescriptor.getIndex())
                .setType(Meta.Type.REQUEST)
                .setContent(methodInfo.getRequest().toByteString())
                .build();
    }
}

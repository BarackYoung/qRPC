package com.qrpc.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.qrpc.Meta;
import com.qrpc.MethodInfoHolder;
import com.qrpc.exception.UnKnownResponseException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Client method receive message from server, all client stub share the same inboundHandler here,
 * when channelRead() invoked, we get a @Mata.MetaData, from which we can get serviceName and methodId.
 * we find specific callback function RpcController and RpcCallback and invoke them.
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/8
 **/
@ChannelHandler.Sharable
@Slf4j
public class DefaultChannelInboundHandler extends SimpleChannelInboundHandler<Meta.RpcMetaData> {

    private final RequestCache requestCache;
    private final RequestFuture requestFuture;

    public DefaultChannelInboundHandler(RequestCache requestCache, RequestFuture requestFuture) {
        this.requestCache = requestCache;
        this.requestFuture = requestFuture;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) {
        String requestId = metaData.getRequestId();
        log.trace("request Id: {}", requestId);
        if (!requestCache.contains(requestId)) {
            log.error("respond a unknown response");
            throw new UnKnownResponseException();
        }
        MethodInfoHolder methodInfo = requestCache.get(requestId);
        Message response;
        try {
            response = methodInfo.getResponsePrototype().getParserForType().parseFrom(metaData.getContent());
        } catch (InvalidProtocolBufferException e) {
            methodInfo.getController().setFailed("response content can not parse to type");
            log.error("response content can not parse to type: {}", methodInfo.getResponsePrototype().getClass());
            e.printStackTrace();
            return;
        }
        if (requestFuture.contains(requestId)) {
            requestFuture.get(requestId).complete(response);
            return;
        }
        methodInfo.getCallback().run(response);
        requestCache.remove(requestId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("An exception detected");
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Channel to {} active", ctx.channel().remoteAddress());
    }
}

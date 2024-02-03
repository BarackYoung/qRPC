package org.qrpc.net;

import org.qrpc.Meta;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.qrpc.server.ListableServiceFactory;
import org.qrpc.server.ServiceExecutor;
import org.qrpc.server.ServiceFactory;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
@Slf4j
public class ProtobufInboundHandler extends SimpleChannelInboundHandler<Meta.RpcMetaData> {
    private final ServiceFactory serviceFactory = ListableServiceFactory.getInstance();

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) {
        Sender nettySeder = new NettySender(channelHandlerContext);
        new ServiceExecutor(nettySeder).onMessage(metaData);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.fireExceptionCaught(cause);
    }
}

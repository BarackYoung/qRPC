package org.qrpc.net;

import org.qrpc.Meta;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.qrpc.server.ServiceExecutor;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
@Slf4j
public class NettyServerInboundHandler extends SimpleChannelInboundHandler<Meta.RpcMetaData> {

    private final Responsible responsible;

    public NettyServerInboundHandler(Responsible responsible) {
        this.responsible = responsible;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) {
        Sender nettySeder = new NettyServerSender(channelHandlerContext);
        responsible.onMessage(metaData, nettySeder);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error(cause.getMessage());
        ctx.fireExceptionCaught(cause);
    }
}

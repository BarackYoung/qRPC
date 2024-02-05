package org.qrpc.net;

import org.qrpc.Meta;
import org.qrpc.client.ResponseHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
public class NettyClientInboundHandler extends SimpleChannelInboundHandler<Meta.RpcMetaData> {

    private final Receiver receiver;

    public NettyClientInboundHandler(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) {
        receiver.onMessage(metaData);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }
}

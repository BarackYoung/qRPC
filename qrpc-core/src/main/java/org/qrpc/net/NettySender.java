package org.qrpc.net;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class NettySender implements Sender {
    private final ChannelHandlerContext channelHandlerContext;

    public NettySender(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void send(Object o) {
        channelHandlerContext.writeAndFlush(o);
    }
}

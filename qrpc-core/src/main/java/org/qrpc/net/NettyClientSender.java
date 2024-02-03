package org.qrpc.net;

import org.qrpc.exception.ClientChannelNotActive;
import io.netty.channel.*;

/**
 * 使用netty发送的实现类
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class NettyClientSender implements Sender {

    private final Channel channel;

    public NettyClientSender(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void send(Object o) {
        channel.writeAndFlush(o);
    }
}

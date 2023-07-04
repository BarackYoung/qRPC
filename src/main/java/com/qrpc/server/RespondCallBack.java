package com.qrpc.server;

import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.qrpc.Meta;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class RespondCallBack implements RpcCallback<Message> {

    private ChannelHandlerContext channelHandlerContext;

    public RespondCallBack(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    @Override
    public void run(Message parameter) {

    }
}

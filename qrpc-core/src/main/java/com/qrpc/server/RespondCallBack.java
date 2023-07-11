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

    private final ChannelHandlerContext channelHandlerContext;
    private final Meta.RpcMetaData metaData;
    private final String requestId;

    public RespondCallBack(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) {
        this.channelHandlerContext = channelHandlerContext;
        this.metaData = metaData;
        this.requestId = metaData.getRequestId();
    }

    @Override
    public void run(Message response) {
        Meta.RpcMetaData rpcMetaData = Meta.RpcMetaData.newBuilder()
                .setStatus(Meta.Status.SUCCESS)
                .setServiceName(this.metaData.getServiceName())
                .setMethodId(this.metaData.getMethodId())
                .setType(Meta.Type.RESPOND)
                .setContent(response.toByteString())
                .setRequestId(this.requestId)
                .build();
        channelHandlerContext.writeAndFlush(rpcMetaData);
    }
}

package com.qrpc.server;

import com.qrpc.ListableServiceFactory;
import com.qrpc.Meta;
import com.qrpc.ServiceFactory;
import com.qrpc.ServiceHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class ProtobufInboundHandler extends SimpleChannelInboundHandler<Meta.RpcMetaData> {
    private ServiceFactory serviceFactory = ListableServiceFactory.getInstance();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Meta.RpcMetaData metaData) throws Exception {
        String serviceName = metaData.getServiceName();
        int methodIndex = metaData.getMethodId();
        if (!serviceFactory.contains(serviceName)) {
            Meta.RpcMetaData errData = Meta.RpcMetaData.newBuilder()
                    .setStatus(Meta.Status.SERVICE_NOT_FOUND)
                    .setType(Meta.Type.RESPOND)
                    .build();
            channelHandlerContext.writeAndFlush(errData);
        }
        ServiceHolder targetService = serviceFactory.getService(serviceName);
        targetService.callMethod(serviceName, methodIndex, new RPCController(), metaData, new RespondCallBack(channelHandlerContext));
    }
}

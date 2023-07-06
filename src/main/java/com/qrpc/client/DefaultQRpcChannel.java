package com.qrpc.client;

import com.google.protobuf.*;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public class DefaultQRpcChannel implements RpcChannel {

    @Override
    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype, RpcCallback<Message> done) {

    }
}

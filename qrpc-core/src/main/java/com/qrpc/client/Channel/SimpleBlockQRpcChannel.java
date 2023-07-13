package com.qrpc.client.Channel;

import com.google.protobuf.*;
import com.qrpc.client.ConnectionFactory;
import com.qrpc.client.EncryptType;
import com.qrpc.client.QRpcClientEndpoint;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/10
 **/
public class SimpleBlockQRpcChannel extends QRpcChannel implements BlockingRpcChannel {
    private final QRpcClientEndpoint endpoint;

    private SimpleBlockQRpcChannel(Builder builder) {
        super(builder);
        this.endpoint = ConnectionFactory.getInstance().getEndpoint(getConnectionConfig());
    }

    @Override
    public Message callBlockingMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype) {
        return endpoint.blockSend(getMethodInfo(method, controller, request, responsePrototype, null));
    }

    public static BlockingQRpcChannelBuilder forAddress(String ip, int port) {
        return new BlockingQRpcChannelBuilder(ip, port);
    }

    public static class BlockingQRpcChannelBuilder extends Builder {
        public BlockingQRpcChannelBuilder(String ip, int port) {
            super(ip, port);
        }
        public BlockingQRpcChannelBuilder userPlaintext() {
            this.encryptType = EncryptType.PLAIN_TEXT;
            return this;
        }
        public SimpleBlockQRpcChannel build() {
            return new SimpleBlockQRpcChannel(this);
        }
    }

}

package org.qrpc.client.Channel;

import com.google.protobuf.*;
import org.qrpc.server.MethodInfoHolder;
import org.qrpc.client.ConnectionFactory;
import org.qrpc.client.EncryptType;
import org.qrpc.client.QRpcClientEndpoint;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/8
 **/
public class SimpleQRpcChannel extends QRpcChannel implements RpcChannel {
    private final QRpcClientEndpoint endpoint;

    @Override
    public void callMethod(Descriptors.MethodDescriptor method, RpcController controller, Message request, Message responsePrototype, RpcCallback<Message> done) {
        MethodInfoHolder methodInfo = getMethodInfo(method, controller, request, responsePrototype, done);
        endpoint.send(methodInfo);
    }

    private SimpleQRpcChannel(Builder builder) {
        super(builder);
        this.endpoint = ConnectionFactory.getInstance().getEndpoint(getConnectionConfig());
    }

    public static SimpleQRpcChannelBuilder forAddress(String ip, int port) {
        return new SimpleQRpcChannelBuilder(ip, port);
    }

    public static class SimpleQRpcChannelBuilder extends Builder {

        public SimpleQRpcChannelBuilder(String ip, int port) {
            super(ip, port);
        }

        public SimpleQRpcChannelBuilder userPlaintext() {
            this.encryptType = EncryptType.PLAIN_TEXT;
            return this;
        }

        public SimpleQRpcChannel build() {
            return new SimpleQRpcChannel(this);
        }
    }

}

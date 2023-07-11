package com.qrpc.client.Channel;

import com.google.protobuf.*;
import com.qrpc.MethodInfoHolder;
import com.qrpc.client.ConnectionConfig;
import com.qrpc.client.EncryptType;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public abstract class QRpcChannel {
    protected String ip;
    protected int port;
    protected EncryptType encryptType;

    public QRpcChannel(Builder builder) {
        this.ip = builder.ip;
        this.port = builder.port;
        this.encryptType = builder.encryptType;
    }

    protected ConnectionConfig getConnectionConfig() {
        return ConnectionConfig.builder()
                .ip(this.ip)
                .port(this.port)
                .encryptType(this.encryptType)
                .build();
    }

    protected MethodInfoHolder getMethodInfo(Descriptors.MethodDescriptor method, RpcController controller
            , Message request, Message responsePrototype, RpcCallback<Message> done) {
        return MethodInfoHolder.builder()
                .method(method)
                .controller(controller)
                .request(request)
                .responsePrototype(responsePrototype)
                .callback(done)
                .build();
    }

    public static class Builder {
        protected String ip;
        protected int port;
        protected EncryptType encryptType;

        public Builder ip(String ip) {
            this.ip = ip;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }
    }
}

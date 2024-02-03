package org.qrpc.client.Channel;

import com.google.protobuf.*;
import org.qrpc.server.MethodInfoHolder;
import org.qrpc.client.ConnectionConfig;
import org.qrpc.client.EncryptType;

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
        return new ConnectionConfig(this.ip, this.port, this.encryptType);
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

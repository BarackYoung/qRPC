package org.qrpc.server;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import lombok.Getter;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/8
 **/
@Getter
public class MethodInfoHolder {
    private Descriptors.MethodDescriptor method;
    private RpcController controller;
    private com.google.protobuf.Message request;
    private Message responsePrototype;
    private RpcCallback<Message> callback;

    public static MethodInfoHolder builder() {
        return new MethodInfoHolder();
    }

    public MethodInfoHolder method(Descriptors.MethodDescriptor methodDescriptor) {
        this.method = methodDescriptor;
        return this;
    }

    public MethodInfoHolder controller(RpcController controller) {
        this.controller = controller;
        return this;
    }

    public MethodInfoHolder request(com.google.protobuf.Message message) {
        this.request = message;
        return this;
    }

    public MethodInfoHolder responsePrototype(Message responsePrototype) {
        this.responsePrototype = responsePrototype;
        return this;
    }

    public MethodInfoHolder callback(RpcCallback<Message> callback) {
        this.callback = callback;
        return this;
    }

    public MethodInfoHolder build() {
        return this;
    }

}

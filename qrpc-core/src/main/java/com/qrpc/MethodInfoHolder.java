package com.qrpc;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/8
 **/
@SuperBuilder
@Getter
public class MethodInfoHolder {
    private Descriptors.MethodDescriptor method;
    private RpcController controller;
    private com.google.protobuf.Message request;
    private Message responsePrototype;
    private RpcCallback<Message> callback;
}

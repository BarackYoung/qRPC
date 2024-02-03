package org.qrpc.server;

import com.google.protobuf.*;
import org.qrpc.Meta;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/3
 **/
public class ServiceHolder {
    private final Service service;

    public ServiceHolder(Service service) {
        this.service = service;
    }

    public void callMethod(int methodId, RpcController controller, Meta.RpcMetaData meta, RpcCallback<Message> callback) {
        Descriptors.MethodDescriptor methodDescriptor = this.service.getDescriptorForType().getMethods().get(methodId);
        Message request;
        try {
            request = this.service.getRequestPrototype(methodDescriptor).getParserForType().parseFrom(meta.getContent());
        } catch (InvalidProtocolBufferException e) {
            controller.setFailed("InvalidProtocolBuffer");
            return;
        }
        this.service.callMethod(methodDescriptor, controller, request, callback);
    }
}

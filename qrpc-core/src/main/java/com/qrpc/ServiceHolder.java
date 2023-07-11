package com.qrpc;

import com.google.protobuf.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
@Slf4j
public class ServiceHolder {
    private final String serviceName;
    private final Service service;
    private final ServiceFactory serviceFactory;

    public ServiceHolder(Service service, ServiceFactory serviceFactory) {
        this.serviceName = service.getDescriptorForType().getFullName();
        this.service = service;
        this.serviceFactory = serviceFactory;
    }

    public Service getService() {
        return this.service;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public ServiceFactory getServiceFactory() {
        return this.serviceFactory;
    }

    public void callMethod(String ServiceName, int methodId, RpcController controller, Meta.RpcMetaData meta, RpcCallback<Message> callback) {
        Descriptors.MethodDescriptor methodDescriptor = this.service.getDescriptorForType().getMethods().get(methodId);
        Message request;
        try {
            request = this.service.getRequestPrototype(methodDescriptor).getParserForType().parseFrom(meta.getContent());
        } catch (InvalidProtocolBufferException e) {
            log.error("content in meta is not a valid protobuf byteString");
            controller.setFailed("InvalidProtocolBuffer");
            return;
        }
        this.service.callMethod(methodDescriptor, controller, request, callback);
    }
}

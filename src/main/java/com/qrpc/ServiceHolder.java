package com.qrpc;

import com.google.protobuf.Service;
import com.google.protobuf.RpcController;
import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.Descriptors;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
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

    void callMethod(String ServiceName, int methodId, RpcController controller, Message request, RpcCallback<Message> callback) {
        Descriptors.MethodDescriptor methodDescriptor = this.service.getDescriptorForType().getMethods().get(methodId);
        this.service.callMethod(methodDescriptor, controller, request, callback);
    }
}

package com.qrpc;

import com.google.protobuf.Service;
import com.qrpc.exception.DuplicateServiceException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
public class ListableServiceFactory implements ServiceFactory {

    private static volatile ServiceFactory serviceFactory;
    private final ConcurrentHashMap<String, ServiceHolder> serviceMap = new ConcurrentHashMap<>();

    @Override
    public void register(Service service) {
        ServiceHolder serviceHolder = new ServiceHolder(service, this);
        register(service.getDescriptorForType().getFullName(), serviceHolder);
    }

    @Override
    public void register(String name, ServiceHolder serviceHolder) {
        if (serviceMap.contains(name)) {
            throw new DuplicateServiceException();
        }
        serviceMap.put(name, serviceHolder);
    }

    @Override
    public boolean contains(String name) {
        return serviceMap.contains(name);
    }

    @Override
    public ServiceHolder get(String name) {
        return serviceMap.get(name);
    }

    public static ServiceFactory getInstance() {
        if (serviceFactory != null) {
            return serviceFactory;
        }
        synchronized (ListableServiceFactory.class) {
            if (serviceFactory != null) {
                return serviceFactory;
            }
            serviceFactory = new ListableServiceFactory();
            return serviceFactory;
        }
    }
}

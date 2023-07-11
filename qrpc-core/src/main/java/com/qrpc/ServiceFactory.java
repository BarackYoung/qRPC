package com.qrpc;

import com.google.protobuf.Service;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
public interface ServiceFactory extends Factory<ServiceHolder> {
    void register(Service service);

    void register(String name, ServiceHolder serviceHolder);

    boolean contains(String name);

    ServiceHolder get(String name);
}

package com.qrpc;

import com.google.protobuf.Service;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
public interface ServiceFactory {
    void register(Service service);

    void register(String name, ServiceHolder serviceHolder);

    ServiceHolder getService(String name);
}

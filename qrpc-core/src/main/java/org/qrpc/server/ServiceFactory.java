package org.qrpc.server;

import com.google.protobuf.Service;
import org.qrpc.Factory;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/4/3
 **/
public interface ServiceFactory extends Factory<ServiceHolder> {
    void register(Service service);

    void register(String name, ServiceHolder serviceHolder);

    boolean contains(String name);

    ServiceHolder get(String name);
}

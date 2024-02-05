package org.qrpc.server;

import com.google.protobuf.Service;
import org.qrpc.net.Server;
import org.qrpc.net.NettyTCPServer;
import lombok.Getter;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/9
 **/
@Getter
public class ServerBuilder {
    private final int port;
    private final ServiceFactory serviceFactory = ListableServiceFactory.getInstance();
    private ServerBuilder(int port) {
        this.port = port;
    }
    public static ServerBuilder forPort(int port) {
        return new ServerBuilder(port);
    }

    public ServerBuilder addService(Service service) {
        serviceFactory.register(service);
        return this;
    }

    public Server build() {
        return new NettyTCPServer(this);
    }

}

package com.qrpc;

import com.qrpc.server.Server;
import com.qrpc.server.TCPServer;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class Test {
    public static void main(String[] args) {
        Server server = new TCPServer();
        server.start(8888);
    }
}

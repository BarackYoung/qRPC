package com.qrpc.client;

import com.qrpc.Factory;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public class NioQRpcClientFactory implements Factory<QRpcClientEndpoint<?,?>> {
    @Override
    public void register(String name, QRpcClientEndpoint<?, ?> serviceHolder) {

    }

    @Override
    public boolean contains(String name) {
        return false;
    }

    @Override
    public QRpcClientEndpoint<?, ?> get(String name) {
        return null;
    }
}

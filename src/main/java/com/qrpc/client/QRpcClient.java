package com.qrpc.client;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public interface QRpcClient<A, B> {
    void send(String ip, int port, A request, ResponseCallback<B> responseCallback);
}

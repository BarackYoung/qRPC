package com.qrpc.client;

import com.google.protobuf.Message;
import com.qrpc.MethodInfoHolder;

/**
 * send a message to ip:port, responseCallback invoked when respond
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public interface QRpcClientEndpoint {
    void send(MethodInfoHolder methodInfo);

    Message blockSend(MethodInfoHolder methodInfo);

    boolean available();
}

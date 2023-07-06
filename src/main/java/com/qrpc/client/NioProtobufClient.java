package com.qrpc.client;

import com.google.protobuf.Message;

/**
 * @version 1.0.0
 * @author Yang Lianhuan
 * @since 2023/7/6
 **/
public class NioProtobufClient implements QRpcClient<Message, Message> {

    @Override
    public void send(String ip, int port, Message request, ResponseCallback<Message> responseCallback) {

    }
}

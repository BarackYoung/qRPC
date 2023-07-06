package com.qrpc.client;

import com.google.protobuf.Message;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public class DefaultClient implements QRpcClient<Message, Message> {

    private NioQRpcClientFactory clientFactory;


    @Override
    public void send(String ip, int port, Message request, ResponseCallback<Message> responseCallback) {

    }
}

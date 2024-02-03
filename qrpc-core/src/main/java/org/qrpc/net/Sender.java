package org.qrpc.net;

/**
 * 此为发送消息实现类，底层通信框架实现此接口发送消息
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public interface Sender {
    void send(Object o);
}

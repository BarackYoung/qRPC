package org.qrpc.net;

/**
 * 消息接收器，底层网络框架实现此接口接收消息
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public interface Receiver {
    void onMessage(Object object);
}

package org.qrpc.server;

import lombok.Getter;
import org.qrpc.net.Receiver;
import org.qrpc.net.Responsible;
import org.qrpc.net.Sender;
import org.qrpc.net.Server;

/**
 * 需要实现start方法来启动服务，需要回调onMessage来接收消息并传入消息回复器
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/4
 **/
@Getter
public abstract class AbstractServer implements Server {

    @Override
    public void onMessage(Object o, Sender responder) {
        // 因为每次客户端可能不一样，因此每次收到消息都创建一个ServiceExecutor来应答消息，防止消息走串
        new ServiceExecutor(responder).onMessage(o);
    }
}

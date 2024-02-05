package org.qrpc.net;

/**
 * 服务器，底层通信框架实现此接口启动服务
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/1
 **/
public interface Server extends Responsible {
    void start();
}

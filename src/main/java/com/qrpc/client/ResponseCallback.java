package com.qrpc.client;

/**
 * method run will be called when server return a message T to client
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public interface ResponseCallback<T>{
    void run(T response);
}

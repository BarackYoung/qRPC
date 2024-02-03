package org.qrpc.exception;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class ClientChannelNotActive extends RuntimeException{
    public ClientChannelNotActive() {
        super("client channel is not active");
    }
}

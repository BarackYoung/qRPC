package org.qrpc.net;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/4
 **/
public interface Responsible {
    void onMessage(Object o, Sender responder);
}

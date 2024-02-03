package org.qrpc.client;

import lombok.Getter;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/10
 **/
@Getter
public class ConnectionConfig {
    private final String ip;
    private final int port;
    private final EncryptType encryptType;

    public ConnectionConfig(String ip, int port, EncryptType encryptType) {
        this.ip = ip;
        this.port = port;
        this.encryptType = encryptType;
    }

}

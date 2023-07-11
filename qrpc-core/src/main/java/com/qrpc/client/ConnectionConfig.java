package com.qrpc.client;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/10
 **/
@Data
@SuperBuilder
public class ConnectionConfig {
    private String ip;
    private int port;
    private EncryptType encryptType;
}

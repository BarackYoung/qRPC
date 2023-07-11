package com.qrpc.client;

import com.qrpc.Factory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/10
 **/
public class ConnectionFactory implements Factory<QRpcClientEndpoint> {
    private static final String COLON = ":";
    private static ConnectionFactory connectionFactory;
    private static final Map<String, QRpcClientEndpoint> connectionMap = new ConcurrentHashMap<>();
    private final List<EndpointHandler> endpointHandlers = new ArrayList<>();

    public static ConnectionFactory getInstance() {
        if (connectionFactory != null) {
            return connectionFactory;
        }
        synchronized (ConnectionFactory.class) {
            if (connectionFactory != null) {
                return connectionFactory;
            }
            connectionFactory = new ConnectionFactory();
            return connectionFactory;
        }
    }
    private ConnectionFactory() {
        this.endpointHandlers.add(new PlainTextEndpointHandler());
        this.endpointHandlers.sort(Comparator.comparingInt(EndpointHandler::getOrder));
    };

    public QRpcClientEndpoint getEndpoint(ConnectionConfig connectionConfig) {
        String ip = connectionConfig.getIp();
        int port = connectionConfig.getPort();
        String key = ip + COLON + port;
        QRpcClientEndpoint endpoint = get(key);
        if (endpoint != null && endpoint.available()) {
            return endpoint;
        }
        remove(key);
        endpoint = getNewEndpoint(connectionConfig);
        register(key, endpoint);
        return endpoint;
    }

    private QRpcClientEndpoint getNewEndpoint(ConnectionConfig config) {
        for (EndpointHandler handler : this.endpointHandlers) {
            QRpcClientEndpoint endpoint = handler.process(config);
            if (endpoint != null) {
                return endpoint;
            }
        }
        return null;
    }

    @Override
    public void register(String key, QRpcClientEndpoint value) {
        connectionMap.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return connectionMap.containsKey(key);
    }

    @Override
    public QRpcClientEndpoint get(String key) {
        return connectionMap.get(key);
    }

    @Override
    public void remove(String key) {
        connectionMap.remove(key);
    }

    interface EndpointHandler {
        int getOrder();

        QRpcClientEndpoint process(ConnectionConfig config);
    }

    static class PlainTextEndpointHandler implements EndpointHandler {

        private static final int ORDER = 0;

        @Override
        public int getOrder() {
            return ORDER;
        }

        @Override
        public QRpcClientEndpoint process(ConnectionConfig config) {
            if (config.getEncryptType() == EncryptType.PLAIN_TEXT
                    || config.getEncryptType() == null) {
                return new PlainClientEndpoint(config.getIp(), config.getPort());
            }
            return null;
        }
    }
}

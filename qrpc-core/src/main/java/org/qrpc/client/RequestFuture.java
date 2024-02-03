package org.qrpc.client;

import com.google.protobuf.Message;
import org.qrpc.Factory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/10
 **/
public class RequestFuture implements Factory<CompletableFuture<Message>> {

    private final Map<String, CompletableFuture<Message>> futureMap = new ConcurrentHashMap<>();

    @Override
    public void register(String key, CompletableFuture<Message> value) {
        if (!futureMap.containsKey(key)) {
            futureMap.put(key, value);
        }
    }

    @Override
    public boolean contains(String key) {
        return futureMap.containsKey(key);
    }

    @Override
    public CompletableFuture<Message> get(String key) {
        return futureMap.get(key);
    }

    @Override
    public void remove(String key) {
        futureMap.remove(key);
    }
}

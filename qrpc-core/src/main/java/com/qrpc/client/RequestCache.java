package com.qrpc.client;

import com.qrpc.Factory;
import com.qrpc.MethodInfoHolder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/8
 **/
public class RequestCache implements Factory<MethodInfoHolder> {

    private final Map<String, MethodInfoHolder> requestMap = new ConcurrentHashMap<>();

    @Override
    public void register(String name, MethodInfoHolder bean) {
        if (requestMap.containsKey(name)) {
            return;
        }
        requestMap.put(name, bean);
    }

    @Override
    public boolean contains(String name) {
        return requestMap.containsKey(name);
    }

    @Override
    public MethodInfoHolder get(String name) {
        return requestMap.get(name);
    }

    @Override
    public void remove(String key) {
        requestMap.remove(key);
    }
}

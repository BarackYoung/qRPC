package com.qrpc;

import com.google.protobuf.Service;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public interface Factory<T> {
    void register(String key, T value);

    boolean contains(String key);

    T get(String key);

    void remove(String key);
}

package com.qrpc;

import com.google.protobuf.Service;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/6
 **/
public interface Factory<T> {
    void register(String name, T serviceHolder);

    boolean contains(String name);

    T get(String name);
}

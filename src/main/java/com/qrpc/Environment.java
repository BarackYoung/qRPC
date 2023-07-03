package com.qrpc;

import java.util.List;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
public interface Environment {
    Object getProperty(String key);

    List<Object> getProperties(String key);
}

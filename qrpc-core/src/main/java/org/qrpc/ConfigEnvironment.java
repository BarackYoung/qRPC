package org.qrpc;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class ConfigEnvironment implements Environment {

    private ConfigEnvironment() {}
    private static Environment configEnvironment;
    private static final ConcurrentHashMap<String, Object> configs = new ConcurrentHashMap<>();

    static {
        configs.put(PropertyKeys.CLIENT_TIMEOUT, "300000");
    }

    @Override
    public Object getProperty(String key) {
        return configs.get(key);
    }

    public static Environment getInstance() {
        if (!Objects.isNull(configEnvironment)) {
            return configEnvironment;
        }
        synchronized (ConfigEnvironment.class) {
            if (!Objects.isNull(configEnvironment)) {
                return configEnvironment;
            }
            configEnvironment = new ConfigEnvironment();
            return configEnvironment;
        }
    }
}

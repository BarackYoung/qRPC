package com.qrpc.server;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
@Slf4j
public class qRpcController implements RpcController {

    @Override
    public void reset() {
        log.info("reset");
    }

    @Override
    public boolean failed() {
        log.info("failed");
        return false;
    }

    @Override
    public String errorText() {
        log.info("error text");
        return null;
    }

    @Override
    public void startCancel() {
        log.info("startCancel");

    }

    @Override
    public void setFailed(String reason) {
        log.info("setFailed: {}", reason);
    }

    @Override
    public boolean isCanceled() {
        log.info("isCanceled");
        return false;
    }

    @Override
    public void notifyOnCancel(RpcCallback<Object> callback) {
        log.info("notifyOnCancel");
    }
}

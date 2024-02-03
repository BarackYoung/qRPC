package org.qrpc.server;

import org.qrpc.Meta;
import org.qrpc.net.Sender;

import java.util.Objects;

/**
 * 执行实际的业务逻辑，业务逻辑不能在IO线程中处理，否则阻塞IO线程影响处理其他请求
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class ServiceRunner implements Runnable {

    private final ServiceHolder targetService;

    private final Meta.RpcMetaData metaData;

    private final Sender sender;

    public ServiceRunner(Sender sender, ServiceHolder targetService, Meta.RpcMetaData metaData) {
        this.targetService = targetService;
        this.metaData = metaData;
        this.sender = sender;
    }

    @Override
    public void run() {
        int methodId = metaData.getMethodId();
        QRpcController qRpcController = new QRpcController(metaData);
        if (!verifyContext()) {
            qRpcController.setFailed("context info invalid, run service failed");
            return;
        }
        RespondCallBack callback = new RespondCallBack(sender, metaData);
        targetService.callMethod(methodId, qRpcController, metaData, callback);
        if (qRpcController.failed() || qRpcController.isCanceled()) {
            qRpcController.notifyOnFailed(callback);
        }
    }

    private boolean verifyContext() {
        return !Objects.isNull(targetService) && !Objects.isNull(metaData) && !Objects.isNull(sender);
    }
}

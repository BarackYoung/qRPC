package org.qrpc.server;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import org.qrpc.Meta;
import org.qrpc.RpcStatus;
import org.qrpc.utils.ResponseManager;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class QRpcController implements RpcController {
    private RpcStatus status;
    private String reason;
    private final Meta.RpcMetaData metaData;

    public QRpcController(Meta.RpcMetaData metaData) {
        this.metaData = metaData;
    }

    @Override
    public void reset() {
        status = RpcStatus.INIT;
    }

    @Override
    public boolean failed() {
        return this.status == RpcStatus.FAILED;
    }

    @Override
    public String errorText() {
        return reason;
    }

    @Override
    public void startCancel() {
        this.status = RpcStatus.CANCELING;
    }

    @Override
    public void setFailed(String reason) {
        this.status = RpcStatus.FAILED;
        this.reason = reason;
    }

    @Override
    public boolean isCanceled() {
        return this.status == RpcStatus.CANCELED;
    }

    @Override
    public void notifyOnCancel(RpcCallback<Object> callback) {
        Meta.RpcMetaData errData = ResponseManager.getFailedResponse(this.metaData, Meta.Status.SYSTEM_ERROR);
        callback.run(errData);
    }

    public void notifyOnFailed(RespondCallBack callback) {
        Meta.RpcMetaData errData = ResponseManager.getFailedResponse(this.metaData, Meta.Status.SYSTEM_ERROR);
        callback.run(errData);
    }
}

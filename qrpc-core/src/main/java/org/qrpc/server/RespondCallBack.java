package org.qrpc.server;

import com.google.protobuf.Message;
import com.google.protobuf.RpcCallback;
import org.qrpc.Meta;
import org.qrpc.net.Sender;
import org.qrpc.utils.ResponseManager;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/4
 **/
public class RespondCallBack implements RpcCallback<Message> {

    private final Sender sender;
    private final Meta.RpcMetaData metaData;

    public RespondCallBack(Sender sender, Meta.RpcMetaData metaData) {
        this.sender = sender;
        this.metaData = metaData;
    }

    @Override
    public void run(Message response) {
        Meta.RpcMetaData rpcMetaData = ResponseManager.getSuccessResponse(this.metaData, response);
        sender.send(rpcMetaData);
    }
}

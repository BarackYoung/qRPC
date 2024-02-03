package org.qrpc.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.qrpc.Meta;
import org.qrpc.exception.UnKnownResponseException;
import org.qrpc.net.Receiver;
import org.qrpc.server.MethodInfoHolder;
import lombok.Getter;

/**
 * 返回体处理
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
@Getter
public class ResponseHandler implements Receiver {

    private final RequestCache requestCache;
    private final RequestFuture requestFuture;

    public ResponseHandler(RequestCache requestCache, RequestFuture requestFuture) {
        this.requestCache = requestCache;
        this.requestFuture = requestFuture;
    }

    @Override
    public void onMessage(Object object) {
        Meta.RpcMetaData metaData = (Meta.RpcMetaData) object;
        String requestId = metaData.getRequestId();
        if (!requestCache.contains(requestId)) {
            throw new UnKnownResponseException();
        }
        MethodInfoHolder methodInfo = requestCache.get(requestId);
        Message response;
        try {
            response = methodInfo.getResponsePrototype().getParserForType().parseFrom(metaData.getContent());
        } catch (InvalidProtocolBufferException e) {
            methodInfo.getController().setFailed("response content can not parse to type");
            e.printStackTrace();
            return;
        }
        if (requestFuture.contains(requestId)) {
            requestFuture.get(requestId).complete(response);
            requestFuture.remove(requestId);
            return;
        }
        methodInfo.getCallback().run(response);
        requestCache.remove(requestId);
    }
}

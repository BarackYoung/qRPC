package org.qrpc.client;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import org.qrpc.ConfigEnvironment;
import org.qrpc.Environment;
import org.qrpc.Meta;
import org.qrpc.PropertyKeys;
import org.qrpc.net.Receiver;
import org.qrpc.net.Sender;
import org.qrpc.server.MethodInfoHolder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 子类必须调用setSender方法设置消息发送器，接收消息则回调onMessage()方法
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public abstract class Endpoint implements QRpcClientEndpoint, Receiver, Sender {
    protected final RequestFuture requestFuture;
    protected final RequestCache requestCache;

    @Getter
    protected final ResponseHandler responseHandler;
    protected final Environment environment = ConfigEnvironment.getInstance();

    @Setter
    protected boolean ready = false;

    public Endpoint() {
        this.requestCache = new RequestCache();
        this.requestFuture = new RequestFuture();
        this.responseHandler = new ResponseHandler(requestCache ,requestFuture);
    }

    @Override
    public void send(MethodInfoHolder methodInfo) {
        Meta.RpcMetaData metaData = getMetaData(methodInfo);
        requestCache.register(metaData.getRequestId(), methodInfo);
        send(metaData);
    }

    @Override
    public Message blockSend(MethodInfoHolder methodInfo) {
        Meta.RpcMetaData metaData = getMetaData(methodInfo);
        CompletableFuture<Message> responseFuture = new CompletableFuture<>();
        requestCache.register(metaData.getRequestId(), methodInfo);
        requestFuture.register(metaData.getRequestId(), responseFuture);
        send(metaData);
        try {
            return responseFuture.get(Long.parseLong(environment.getProperty(PropertyKeys.CLIENT_TIMEOUT).toString()), TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean available() {
        return ready;
    }

    @Override
    public void onMessage(Object o) {
        getResponseHandler().onMessage(o);
    }

    private Meta.RpcMetaData getMetaData(MethodInfoHolder methodInfo) {
        Descriptors.MethodDescriptor mDescriptor = methodInfo.getMethod();
        return Meta.RpcMetaData.newBuilder()
                .setRequestId(UUID.randomUUID().toString())
                .setServiceName(mDescriptor.getService().getFullName())
                .setMethodId(mDescriptor.getIndex())
                .setType(Meta.Type.REQUEST)
                .setContent(methodInfo.getRequest().toByteString())
                .build();
    }
}

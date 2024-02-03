package org.qrpc.client;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import org.qrpc.ConfigEnvironment;
import org.qrpc.Environment;
import org.qrpc.Meta;
import org.qrpc.PropertyKeys;
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
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public abstract class Endpoint implements QRpcClientEndpoint {
    @Getter
    @Setter
    protected Sender sender;
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
        assert sender != null;
        sender.send(metaData);
    }

    @Override
    public Message blockSend(MethodInfoHolder methodInfo) {
        Meta.RpcMetaData metaData = getMetaData(methodInfo);
        CompletableFuture<Message> responseFuture = new CompletableFuture<>();
        requestCache.register(metaData.getRequestId(), methodInfo);
        requestFuture.register(metaData.getRequestId(), responseFuture);
        assert sender != null;
        sender.send(metaData);
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

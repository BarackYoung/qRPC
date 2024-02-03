package org.qrpc.server;

import org.qrpc.Meta;
import org.qrpc.ThreadService;
import org.qrpc.net.Receiver;
import org.qrpc.net.Sender;
import org.qrpc.utils.ResponseManager;

/**
 * 运行业务逻辑
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/2/3
 **/
public class ServiceExecutor implements Receiver {

    private final ServiceFactory serviceFactory = ListableServiceFactory.getInstance();
    private final Sender sender;

    public ServiceExecutor(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void onMessage(Object object) {
        Meta.RpcMetaData metaData = (Meta.RpcMetaData) object;
        String serviceName = metaData.getServiceName();
        if (!serviceFactory.contains(serviceName)) {
            Meta.RpcMetaData errData = ResponseManager.getFailedResponse(metaData, Meta.Status.SERVICE_NOT_FOUND);
            sender.send(errData);
            return;
        }
        ServiceHolder targetService = serviceFactory.get(serviceName);
        ServiceRunner runner = new ServiceRunner(sender, targetService, metaData);
        ThreadService.getBusinessExecutor().submit(runner);
    }
}

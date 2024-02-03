package org.qrpc.utils;

import com.google.protobuf.Message;
import org.qrpc.Meta;

/**
 * 响应体管理
 *
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/1/28
 **/
public class ResponseManager {
    /**
     * 成功响应体
     *
     * @param metaData metaData
     * @param response response
     * @return RpcMetaData
     */
    public static Meta.RpcMetaData getSuccessResponse(Meta.RpcMetaData metaData, Message response) {
        return Meta.RpcMetaData.newBuilder()
                .setStatus(Meta.Status.SUCCESS)
                .setServiceName(metaData.getServiceName())
                .setMethodId(metaData.getMethodId())
                .setType(Meta.Type.RESPOND)
                .setContent(response.toByteString())
                .setRequestId(metaData.getRequestId())
                .build();
    }

    /**
     * 返回失败响应体
     *
     * @param metaData 请求体
     * @param status 状态码
     * @return RpcMetaData
     */
    public static Meta.RpcMetaData getFailedResponse(Meta.RpcMetaData metaData, Meta.Status status) {
        return Meta.RpcMetaData.newBuilder()
                .setStatus(status)
                .setServiceName(metaData.getServiceName())
                .setMethodId(metaData.getMethodId())
                .setType(Meta.Type.RESPOND)
                .setRequestId(metaData.getRequestId())
                .build();
    }
}

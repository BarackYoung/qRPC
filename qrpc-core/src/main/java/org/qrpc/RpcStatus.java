package org.qrpc;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2024/1/27
 **/
public enum RpcStatus {
    /**
     * 初始化
     */
    INIT,

    /**
     * 成功
     */
    SUCCESS,

    /**
     * 失败
     */
    FAILED,

    /**
     * 取消中
     */
    CANCELING,

    /**
     * 已取消
     */
    CANCELED
}

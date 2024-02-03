package org.qrpc.exception;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/9
 **/
public class UnKnownResponseException extends RuntimeException {
    public UnKnownResponseException(String message) {
        super(message);
    }

    public UnKnownResponseException() {
        super("response unknown requestId");
    }
}

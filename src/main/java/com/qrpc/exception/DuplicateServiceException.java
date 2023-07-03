package com.qrpc.exception;

/**
 * @author Yang Lianhuan
 * @version 1.0.0
 * @since 2023/7/3
 **/
public class DuplicateServiceException extends RuntimeException {
    public DuplicateServiceException(String message) {
        super(message);
    }

    public DuplicateServiceException() {
        super("Service already exist.");
    }
}

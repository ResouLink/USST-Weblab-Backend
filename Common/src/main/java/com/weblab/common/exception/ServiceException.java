package com.weblab.common.exception;

/**
 * 业务异常
 */
public class ServiceException extends RuntimeException{
    public ServiceException(String message) {
        super(message);
    }
}

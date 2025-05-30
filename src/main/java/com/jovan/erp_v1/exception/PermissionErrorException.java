package com.jovan.erp_v1.exception;

public class PermissionErrorException extends RuntimeException {

    public PermissionErrorException(String msg) {
        super(msg);
    }
}

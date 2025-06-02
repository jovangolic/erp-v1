package com.jovan.erp_v1.exception;

public class EmailSettingNotFoundException extends RuntimeException {

    public EmailSettingNotFoundException(String msg) {
        super(msg);
    }
}

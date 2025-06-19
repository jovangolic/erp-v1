package com.jovan.erp_v1.exception;

public class AccountNotFoundErrorException extends RuntimeException {

    public AccountNotFoundErrorException(String msg) {
        super(msg);
    }
}

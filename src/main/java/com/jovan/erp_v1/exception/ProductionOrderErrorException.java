package com.jovan.erp_v1.exception;

public class ProductionOrderErrorException extends RuntimeException {

    public ProductionOrderErrorException(String msg) {
        super(msg);
    }
}

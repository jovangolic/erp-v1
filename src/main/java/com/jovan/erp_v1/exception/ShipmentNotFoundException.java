package com.jovan.erp_v1.exception;

public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(String msg) {
        super(msg);
    }
}

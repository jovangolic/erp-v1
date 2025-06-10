package com.jovan.erp_v1.exception;

public class StockTransferErrorException extends RuntimeException {

    public StockTransferErrorException(String msg) {
        super(msg);
    }
}

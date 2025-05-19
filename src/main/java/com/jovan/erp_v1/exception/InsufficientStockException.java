package com.jovan.erp_v1.exception;



public class InsufficientStockException extends RuntimeException {

	public InsufficientStockException(String msg) {
		super(msg);
	}
}

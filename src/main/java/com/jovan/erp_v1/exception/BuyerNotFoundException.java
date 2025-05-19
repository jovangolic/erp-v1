package com.jovan.erp_v1.exception;

public class BuyerNotFoundException extends RuntimeException {

	public BuyerNotFoundException(String msg) {
		super(msg);
	}
	
	public BuyerNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}

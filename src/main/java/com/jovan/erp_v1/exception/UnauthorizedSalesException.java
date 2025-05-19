package com.jovan.erp_v1.exception;


public class UnauthorizedSalesException extends RuntimeException {
	
	public UnauthorizedSalesException(String msg) {
		super(msg);
	}
	
	public UnauthorizedSalesException(String msg, Throwable t) {
		super(msg, t);
	}
}

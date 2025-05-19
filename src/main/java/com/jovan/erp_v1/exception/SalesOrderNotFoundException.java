package com.jovan.erp_v1.exception;


public class SalesOrderNotFoundException extends RuntimeException {

	public SalesOrderNotFoundException(String msg) {
		super(msg);
	}
}

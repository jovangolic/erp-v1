package com.jovan.erp_v1.exception;


public class DuplicateBarCodeException extends RuntimeException {
	
	public DuplicateBarCodeException(String msg) {
		super(msg);
	}
}

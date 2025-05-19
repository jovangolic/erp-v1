package com.jovan.erp_v1.exception;


public class RawMaterialNotFoundException extends RuntimeException {

	public RawMaterialNotFoundException(String msg) {
		super(msg);
	}
	
	public RawMaterialNotFoundException(String msg ,Throwable t) {
		super(msg, t);
	}
}

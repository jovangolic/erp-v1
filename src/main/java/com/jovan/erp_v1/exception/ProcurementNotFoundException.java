package com.jovan.erp_v1.exception;


public class ProcurementNotFoundException extends RuntimeException {
	
	public ProcurementNotFoundException(String msg) {
		super(msg);
	}
	
	public ProcurementNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}
}

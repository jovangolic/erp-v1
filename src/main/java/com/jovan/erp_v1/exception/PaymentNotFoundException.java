package com.jovan.erp_v1.exception;


public class PaymentNotFoundException extends RuntimeException {

	public PaymentNotFoundException(String msg) {
		super(msg);
	}
}

package com.jovan.erp_v1.exception;


public class EmailAlreadyExistsException extends RuntimeException {

	public EmailAlreadyExistsException(String msg) {
		super(msg);
	}
}

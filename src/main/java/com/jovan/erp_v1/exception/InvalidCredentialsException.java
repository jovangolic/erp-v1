package com.jovan.erp_v1.exception;

public class InvalidCredentialsException extends RuntimeException {
	public InvalidCredentialsException(String msg) {
		super(msg);
	}
	public InvalidCredentialsException(String msg, Throwable t) {
		super(msg, t);
	}
}

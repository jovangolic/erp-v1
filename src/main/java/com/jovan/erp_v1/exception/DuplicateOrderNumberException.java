package com.jovan.erp_v1.exception;

public class DuplicateOrderNumberException extends RuntimeException {

	public DuplicateOrderNumberException(String msg) {
		super(msg);
	}
}

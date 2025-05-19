package com.jovan.erp_v1.exception;


public class StorageCapacityExceededException extends RuntimeException {
	
	public StorageCapacityExceededException(String msg) {
		super(msg);
	}
}

package com.jovan.erp_v1.exception;


public class InventoryNotFoundException extends RuntimeException {

	public InventoryNotFoundException(String msg) {
		super(msg);
	}
}

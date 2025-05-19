package com.jovan.erp_v1.exception;


public class RoleAlreadyExistException extends RuntimeException {
	public RoleAlreadyExistException(String message) {
        super(message);
    }
}

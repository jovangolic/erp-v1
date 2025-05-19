package com.jovan.erp_v1.model;

import java.security.SecureRandom;

public class BarCodeGenerator {
	
	private BarCodeGenerator() {
		
	}

	public static String generate() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

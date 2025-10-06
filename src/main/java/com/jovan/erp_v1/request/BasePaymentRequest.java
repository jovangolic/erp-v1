package com.jovan.erp_v1.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePaymentRequest {
	
    protected String merchantId;
    protected BigDecimal amount;
    protected String currency;
    protected String paymentToken;
}

package com.jovan.erp_v1.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *Base apstract class for response object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasePaymentResponse {
    protected String transactionId;
    protected String result;
    protected String responseCode;
    protected String errorMessage;

    public boolean isSuccessful() {
        return "SUCCESS".equalsIgnoreCase(result) || "APPROVED".equalsIgnoreCase(result);
    }
}
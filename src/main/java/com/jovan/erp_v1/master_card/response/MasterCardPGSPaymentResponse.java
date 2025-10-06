package com.jovan.erp_v1.master_card.response;

import com.jovan.erp_v1.response.BasePaymentResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MasterCardPGSPaymentResponse extends BasePaymentResponse {
    
	public MasterCardPGSPaymentResponse(String transactionId, String result, String responseCode, String errorMessage) {
        super(transactionId, result, responseCode, errorMessage);
    }
}
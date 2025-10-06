package com.jovan.erp_v1.master_card.request;

import java.math.BigDecimal;

import com.jovan.erp_v1.request.BasePaymentRequest;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class MasterCardPGSPaymentRequest extends BasePaymentRequest {

    public MasterCardPGSPaymentRequest(String merchantId, BigDecimal amount, String currency, String paymentToken) {
        super(merchantId, amount, currency, paymentToken);
    }
}

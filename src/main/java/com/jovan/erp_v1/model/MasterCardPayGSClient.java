package com.jovan.erp_v1.model;


import java.security.PrivateKey;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.jovan.erp_v1.master_card.request.MasterCardPGSPaymentRequest;
import com.jovan.erp_v1.master_card.response.MasterCardPGSPaymentResponse;

import lombok.Data;

/**
 *MasterCard pattern/client class for payment. This class isn't entity class, it won't be depicted in database
 */
@Component
@Data
public class MasterCardPayGSClient extends AbstractPaymentCardClient<MasterCardPGSPaymentRequest, MasterCardPGSPaymentResponse> {

	private final String baseUrl;
    private final String merchantId;
    private final String apiKey;
    private final String certificateId;

    public MasterCardPayGSClient(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            PrivateKey privateKey,
            @Value("${mpgs.base-url}") String baseUrl,
            @Value("${mpgs.merchant-id}") String merchantId,
            @Value("${mpgs.api-key}") String apiKey,
            @Value("${mpgs.certificate-id}") String certificateId
    ) {
        super(restTemplate, objectMapper, privateKey);
        this.baseUrl = baseUrl;
        this.merchantId = merchantId;
        this.apiKey = apiKey;
        this.certificateId = certificateId;
    }
    
    @Override
    public MasterCardPGSPaymentResponse processPayment(MasterCardPGSPaymentRequest request) throws Exception {
        String url = baseUrl + "/payments";

        Map<String, Object> payload = Map.of(
            "merchant", Map.of("merchantId", request.getMerchantId()),
            "transaction", Map.of("amount", request.getAmount(), "currency", request.getCurrency()),
            "payment", Map.of("paymentToken", request.getPaymentToken())
        );
        String canonical = canonicalJson(payload);
        String signature = signRequest(canonical);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key " + apiKey);
        headers.set("Certificate-Id", certificateId);
        headers.set("Signature", signature);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
        Map<String, Object> body = response.getBody();
        return new MasterCardPGSPaymentResponse(
                (String) body.get("transactionId"),
                (String) body.get("result"),
                (String) body.get("responseCode"),
                (String) body.getOrDefault("errorMessage", "")
        );
    }
    
}

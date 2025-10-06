package com.jovan.erp_v1.model;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jovan.erp_v1.request.BasePaymentRequest;
import com.jovan.erp_v1.response.BasePaymentResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public abstract class AbstractPaymentCardClient <REQ extends BasePaymentRequest, RES extends BasePaymentResponse> {

	protected final RestTemplate restTemplate;
    protected final ObjectMapper objectMapper;
    protected final PrivateKey privateKey;

    /*protected AbstractPaymentClient(RestTemplate restTemplate, ObjectMapper objectMapper, PrivateKey privateKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.privateKey = privateKey;
    }*/
    
    /**
     * Glavna apstraktna metoda – svaka kartica implementira svoju logiku placanja.
     */
    public abstract RES processPayment(REQ request) throws Exception;

    /**
     * Standardni nacin za konverziju objekta u canonical JSON.
     */
    protected String canonicalJson(Object data) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        return objectMapper.writeValueAsString(data);
    }

    /**
     * Standardni RSA potpis za kartice koje koriste PKI (kao MasterCard).
     */
    protected String signRequest(String canonical) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(canonical.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
    }
}

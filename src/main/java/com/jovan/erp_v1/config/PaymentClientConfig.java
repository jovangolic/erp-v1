package com.jovan.erp_v1.config;

import java.security.PrivateKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jovan.erp_v1.model.MasterCardPayGSClient;


@Configuration
public class PaymentClientConfig {


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    //MasterCard
    @Bean
    public MasterCardPayGSClient masterCardPayGSClient(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            PrivateKey masterCardPrivateKey,
            @Value("${mpgs.base-url}") String baseUrl,
            @Value("${mpgs.merchant-id}") String merchantId,
            @Value("${mpgs.api-key}") String apiKey,
            @Value("${mpgs.certificate-id}") String certificateId
    ) {
    	return new MasterCardPayGSClient(
                restTemplate,
                objectMapper,
                masterCardPrivateKey,
                baseUrl,
                merchantId,
                apiKey,
                certificateId
        );
    }
}

package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.LogisticsProvider;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsProviderResponse {

    private Long id;
    private String name;
    private String contactPhone;
    private String email;
    private String website;

    public LogisticsProviderResponse(LogisticsProvider provider) {
        this.id = provider.getId();
        this.name = provider.getName();
        this.contactPhone = provider.getContactPhone();
        this.email = provider.getEmail();
        this.website = provider.getWebsite();
    }
}
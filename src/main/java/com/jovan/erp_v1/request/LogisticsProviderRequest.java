package com.jovan.erp_v1.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LogisticsProviderRequest(
                Long id,
                @NotBlank String name,
                @NotEmpty String contactPhone,
                @NotBlank @Email String email,
                String website) {

}

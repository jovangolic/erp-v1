package com.jovan.erp_v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedOptionRequest {

    @NotNull
    private Long optionId;

    @NotNull
    private Long languageId;

    @NotBlank
    private String localizedLabel;
}

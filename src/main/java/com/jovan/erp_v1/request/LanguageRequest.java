package com.jovan.erp_v1.request;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageRequest {

    @NotNull(message = "Language code type is required")
    private LanguageCodeType languageCodeType;

    @NotNull(message = "Language name type is required")
    private LanguageNameType languageNameType;
}

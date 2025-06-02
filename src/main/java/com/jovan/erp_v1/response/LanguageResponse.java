package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {

    private Long id;
    private LanguageCodeType languageCodeType; // EN, RS, ...
    private LanguageNameType languageNameType;
}

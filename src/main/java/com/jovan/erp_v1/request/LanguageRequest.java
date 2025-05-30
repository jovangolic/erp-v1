package com.jovan.erp_v1.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageRequest {

    private String languageCodeType; // EN, RS, ...
    private String languageNameType;
}

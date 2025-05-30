package com.jovan.erp_v1.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageResponse {

    private Long id;
    private String languageCodeType;
    private String languageNameType;
}

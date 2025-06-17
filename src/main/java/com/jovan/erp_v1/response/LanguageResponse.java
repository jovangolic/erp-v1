package com.jovan.erp_v1.response;

import com.jovan.erp_v1.enumeration.LanguageCodeType;
import com.jovan.erp_v1.enumeration.LanguageNameType;
import com.jovan.erp_v1.model.Language;

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

    public LanguageResponse(Language l) {
        this.id = l.getId();
        this.languageCodeType = l.getLanguageCodeType();
        this.languageNameType = l.getLanguageNameType();
    }
}

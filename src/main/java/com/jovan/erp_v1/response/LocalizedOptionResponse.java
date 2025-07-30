package com.jovan.erp_v1.response;

import com.jovan.erp_v1.model.LocalizedOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedOptionResponse {

    private Long id;
    private OptionResponse optionResponse;
    private LanguageResponse languageResponse;
    private String localizedLabel;

    public LocalizedOptionResponse(LocalizedOption opt) {
        this.id = opt.getId();
        this.optionResponse = opt.getOption() != null ? new OptionResponse(opt.getOption()) : null;
        this.languageResponse = opt.getLanguage() != null ? new LanguageResponse(opt.getLanguage()) : null;
        this.localizedLabel = opt.getLocalizedLabel();
    }
}

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
    private Long optionId;
    private Long languageId;
    private String localizedLabel;

    public LocalizedOptionResponse(LocalizedOption opt) {
        this.id = opt.getId();
        this.optionId = opt.getOption().getId();
        this.languageId = opt.getLanguage().getId();
        this.localizedLabel = opt.getLocalizedLabel();
    }
}

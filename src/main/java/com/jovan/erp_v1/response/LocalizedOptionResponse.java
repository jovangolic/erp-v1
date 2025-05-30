package com.jovan.erp_v1.response;

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
}

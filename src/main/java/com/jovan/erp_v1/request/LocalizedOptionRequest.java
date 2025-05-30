package com.jovan.erp_v1.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedOptionRequest {

    private Long optionId;
    private Long languageId;
    private String localizedLabel;
}

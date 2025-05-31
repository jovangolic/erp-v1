package com.jovan.erp_v1.request;

public record SecuritySettingRequest(
        Long id,
        String settingName,
        String value) {

}

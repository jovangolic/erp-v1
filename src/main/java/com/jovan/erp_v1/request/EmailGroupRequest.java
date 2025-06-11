package com.jovan.erp_v1.request;

import java.util.List;

public record EmailGroupRequest(
        List<String> recipients,
        String subject,
        String body) {

}

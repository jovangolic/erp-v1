package com.jovan.erp_v1.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CompanyEmailBatchRequest(
        @NotEmpty List<@Valid CompanyEmailDTO> employees) {

}

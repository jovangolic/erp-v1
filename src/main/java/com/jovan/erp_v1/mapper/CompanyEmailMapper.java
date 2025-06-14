package com.jovan.erp_v1.mapper;

import org.springframework.stereotype.Component;

import com.jovan.erp_v1.model.CompanyEmail;
import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.response.CompanyEmailResponse;
import com.jovan.erp_v1.response.CompanyEmailResponseDTO;

@Component
public class CompanyEmailMapper {

    public CompanyEmail toEntity(CompanyEmailDTO request) {
        CompanyEmail email = new CompanyEmail();
        email.setFirstName(request.firstName());
        email.setLastName(request.lastName());
        email.setRole(request.types());
        return email;
    }

    public CompanyEmailResponseDTO toResponse(CompanyEmail email) {
        return new CompanyEmailResponseDTO(email.getId(), email.getFirstName(), email.getLastName(), email.getEmail(),
                email.getRole());
    }

    public CompanyEmailResponse toCompanyEmailResponse(CompanyEmail email) {
        return new CompanyEmailResponse(email.getEmail(), email.getFirstName(), email.getLastName(),
                email.getRole(), email.getCreatedAt(), "N/A");
    }
}

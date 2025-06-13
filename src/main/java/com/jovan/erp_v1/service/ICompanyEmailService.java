package com.jovan.erp_v1.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.request.CompanyEmailDTO;
import com.jovan.erp_v1.response.CompanyEmailResponse;

public interface ICompanyEmailService {

    CompletableFuture<String> generateCompanyEmail(String firstName, String lastName);

    /*
     * CompletableFuture<CompanyEmailResponse> createAccountWithCompanyEmail(String
     * firstName, String lastName,
     * RoleTypes role);
     */
    CompletableFuture<CompanyEmailResponse> createAccountWithCompanyEmail(CompanyEmailDTO dto);

    CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users);

    // Nova metoda - vraća jedan company email
    CompletableFuture<CompanyEmailResponse> findOne(String email);

    // Nova metoda - vraća sve company emaile
    CompletableFuture<List<CompanyEmailResponse>> findAll();

    // Nova metoda - briše email
    CompletableFuture<Void> deleteEmail(String email);

}

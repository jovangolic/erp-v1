package com.jovan.erp_v1.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.jovan.erp_v1.enumeration.RoleTypes;
import com.jovan.erp_v1.request.CompanyEmailDTO;

public interface ICompanyEmailService {

    CompletableFuture<String> generateCompanyEmail(String firstName, String lastName);

    CompletableFuture<String> createAccountWithCompanyEmail(String firstName, String lastName, RoleTypes role);

    CompletableFuture<Void> createAllCompanyEmails(List<CompanyEmailDTO> users);
}

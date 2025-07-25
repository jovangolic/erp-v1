package com.jovan.erp_v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.CompanyEmail;

@Repository
public interface CompanyEmailRepository extends JpaRepository<CompanyEmail, Long> {

    boolean existsByEmail(String email);

    Optional<CompanyEmail> findByEmail(String email);
}

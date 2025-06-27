package com.jovan.erp_v1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.MaterialTransaction;

@Repository
public interface MaterialTransactionRepository extends JpaRepository<MaterialTransaction, Long> {

}

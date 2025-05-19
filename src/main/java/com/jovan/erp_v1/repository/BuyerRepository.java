package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

	List<Buyer> findByCompanyName(String companyName);
	
	Optional<Buyer> findByPib(String pib);

	List<Buyer> findByEmailContainingIgnoreCase(String emailFragment);

	List<Buyer> findByCompanyNameContainingIgnoreCase(String companyName);
	
	boolean existsByPib(String pib);
}

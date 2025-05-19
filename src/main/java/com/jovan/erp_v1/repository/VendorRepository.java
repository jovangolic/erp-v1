package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

	List<Vendor> findByName(String name);
	
	Optional<Vendor> findByEmail(String email);
	
	List<Vendor> findByAddress(String address);
}

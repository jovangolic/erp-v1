package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.model.Vendor;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

	List<Vendor> findByNameContainingIgnoreCase(String name);
	Optional<Vendor> findByEmail(String email);
	List<Vendor> findByAddress(String address);
	
	//nove metode
	List<Vendor> findByPhoneNumberLikeIgnoreCase(String phoneNumber);
	@Query("SELECT v FROM Vendor v WHERE LOWER(v.name) LIKE(CONCAT('%', :nameFragment, '%'))")
	List<Vendor> searchByName(@Param("nameFragment") String nameFragment);
	List<Vendor> findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(String name, String address);
	List<Vendor> findByIdBetween(Long startId, Long endId);
	List<Vendor> findByEmailContainingIgnoreCase(String emailFragment);
	List<Vendor> findByPhoneNumberContaining(String phoneNumberFragment);
	@Query("SELECT DISTINCT v FROM Vendor v JOIN v.materialTransactions mt WHERE mt.status = :status")
	List<Vendor> findVendorsByMaterialTransactionStatus(@Param("status") MaterialTransactionStatus status);
	Long countByAddressContainingIgnoreCase(String addressFragment);
	Long countByNameContainingIgnoreCase(String nameFragment);
	Boolean existsByEmail(String email);
	List<Vendor> findAllByOrderByNameAsc();
	List<Vendor> findAllByOrderByNameDesc();
	@Query("SELECT DISTINCT v FROM Vendor v JOIN v.materialTransactions mt WHERE mt.status IN :statuses")
	List<Vendor> findVendorsByTransactionStatuses(@Param("statuses") List<MaterialTransactionStatus> statuses);
}

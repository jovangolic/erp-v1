package com.jovan.erp_v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jovan.erp_v1.model.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

	List<Buyer> findByCompanyName(String companyName);
	Optional<Buyer> findByPib(String pib);
	List<Buyer> findByEmailContainingIgnoreCase(String emailFragment);
	List<Buyer> findByCompanyNameContainingIgnoreCase(String companyName);
	boolean existsByPib(String pib);
	
	//nove metode
	List<Buyer> findByAddressContainingIgnoreCase(String address);
	List<Buyer> findByContactPerson(String contactPerson);
	List<Buyer> findByContactPersonContainingIgnoreCase(String contactPersonFragment);
	List<Buyer> findByPhoneNumberContaining(String phoneFragment);
	List<Buyer> findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String companyName, String address);
	@Query("SELECT b FROM Buyer b WHERE SIZE(b.salesOrders) > 0")
	List<Buyer> findBuyersWithSalesOrders();
	@Query("SELECT b FROM Buyer b WHERE SIZE(b.salesOrders) = 0")
	List<Buyer> findBuyersWithoutSalesOrders();
	boolean existsByEmail(String email);
	@Query("SELECT b FROM Buyer b WHERE " +
		       "(:companyName IS NULL OR LOWER(b.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))) AND " +
		       "(:email IS NULL OR LOWER(b.email) LIKE LOWER(CONCAT('%', :email, '%')))")
	List<Buyer> searchBuyers(@Param("companyName") String companyName,@Param("email") String email);
}

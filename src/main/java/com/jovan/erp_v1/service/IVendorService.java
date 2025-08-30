package com.jovan.erp_v1.service;

import java.util.List;
import com.jovan.erp_v1.enumeration.MaterialTransactionStatus;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;

public interface IVendorService {

	VendorResponse createVendor(VendorRequest request);
	VendorResponse updateVendor(Long id, VendorRequest request);
	void deleteVendor(Long id);
	List<VendorResponse> findByName(String name);
	VendorResponse findByEmail(String email);
	List<VendorResponse> findByAddress(String address);
	VendorResponse getById(Long id);
	List<VendorResponse> getAllVendors();
	
	//nove metode
	List<VendorResponse> findByPhoneNumberLikeIgnoreCase(String phoneNumber);
	List<VendorResponse> searchByName( String nameFragment);
	List<VendorResponse> findByNameIgnoreCaseContainingAndAddressIgnoreCaseContaining(String name, String address);
	List<VendorResponse> findByIdBetween(Long startId, Long endId);
	List<VendorResponse> findByEmailContainingIgnoreCase(String emailFragment);
	List<VendorResponse> findByPhoneNumberContaining(String phoneNumberFragment);
	List<VendorResponse> findVendorsByMaterialTransactionStatus( MaterialTransactionStatus status);
	Long countByAddressContainingIgnoreCase(String addressFragment);
	Long countByNameContainingIgnoreCase(String nameFragment);
	Boolean existsByEmail(String email);
	List<VendorResponse> findAllByOrderByNameAsc();
	List<VendorResponse> findAllByOrderByNameDesc();
	List<VendorResponse> findVendorsByTransactionStatuses( List<MaterialTransactionStatus> statuses);
}

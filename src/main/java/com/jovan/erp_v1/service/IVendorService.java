package com.jovan.erp_v1.service;

import java.util.List;
import java.util.Optional;

import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;

public interface IVendorService {

	VendorResponse createVendor(VendorRequest request);

	VendorResponse updateVendor(Long id, VendorRequest request);

	void deleteVendor(Long id);

	List<VendorResponse> findByName(String name);

	Optional<VendorResponse> findByEmail(String email);

	List<VendorResponse> findByAddress(String address);

	VendorResponse getById(Long id);

	List<VendorResponse> getAllVendors();
}

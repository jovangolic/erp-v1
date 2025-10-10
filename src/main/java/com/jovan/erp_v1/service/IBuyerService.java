package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.enumeration.BuyerStatus;
import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;
import com.jovan.erp_v1.save_as.BuyerSaveAsRequest;
import com.jovan.erp_v1.search_request.BuyerSearchRequest;

public interface IBuyerService {

    BuyerResponse createBuyer(BuyerRequest request);
    BuyerResponse updateBuyer(String pib, BuyerRequest request);
    List<BuyerResponse> getAllBuyers();
    BuyerResponse getBuyerById(Long id);
    void deleteBuyer(Long id);
    List<BuyerResponse> searchBuyer(String keyword);
    BuyerResponse getBuyerByPid(String pib);
    
    //nove metode
    List<BuyerResponse> findByAddressContainingIgnoreCase(String address);
	List<BuyerResponse> findByContactPerson(String contactPerson);
	List<BuyerResponse> findByContactPersonContainingIgnoreCase(String contactPersonFragment);
	List<BuyerResponse> findByPhoneNumberContaining(String phoneFragment);
	List<BuyerResponse> findByCompanyNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String companyName, String address);
	List<BuyerResponse> findBuyersWithSalesOrders();
	List<BuyerResponse> findBuyersWithoutSalesOrders();
	Boolean existsByEmail(String email);
	List<BuyerResponse> searchBuyers( String companyName, String email);
	
	//nove metode
	BuyerResponse trackBuyer( Long id);
	BuyerResponse confirmBuyer( Long id);
	BuyerResponse closeBuyer( Long id);
	BuyerResponse cancelBuyer( Long id);
	BuyerResponse changeStatus( Long id, BuyerStatus status);
	BuyerResponse saveBuyer(BuyerRequest request);
	BuyerResponse saveAs(BuyerSaveAsRequest request);
	List<BuyerResponse> saveAll(List<BuyerRequest> requests);
	List<BuyerResponse> generalSearch(BuyerSearchRequest request);
}

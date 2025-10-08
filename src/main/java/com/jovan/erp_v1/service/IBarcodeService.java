package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;

import com.jovan.erp_v1.enumeration.BarCodeStatus;
import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;
import com.jovan.erp_v1.save_as.BarCodeSaveAsRequest;
import com.jovan.erp_v1.search_request.BarCodeSearchRequest;

public interface IBarcodeService {

	BarCodeResponse createBarCode(BarCodeRequest request);
	BarCodeResponse updateBarCode(Long id, BarCodeRequest request);
	void delete(Long id);
	BarCodeResponse getOne(Long id);
	List<BarCodeResponse> getAll();
	BarCodeResponse getByCode(String code);
	List<BarCodeResponse> findByGoods_Id(Long goodsId);
    List<BarCodeResponse> findByGoods_Name(String goodsName);
    List<BarCodeResponse> findByScannedAtBetween(LocalDateTime from, LocalDateTime to);
    List<BarCodeResponse> findByScannedBy_Id(Long scannedById);
    List<BarCodeResponse> findByScannedBy_FirstNameContainingIgnoreCaseAndScannedBy_LastNameContainingIgnoreCase(
            String userFirstName, String userLastName);
    
    //nove metode
    BarCodeResponse trackBarCode( Long id);
    BarCodeResponse confirmBarCode( Long id);
    BarCodeResponse closeBarCode( Long id);
    BarCodeResponse cancelBarCode( Long id);
    BarCodeResponse changeStatus( Long id, BarCodeStatus status);
    BarCodeResponse saveBarCode(BarCodeRequest request);
    BarCodeResponse saveAs(BarCodeSaveAsRequest request);
    List<BarCodeResponse> saveAll(List<BarCodeRequest> request);
    List<BarCodeResponse> generalSearch(BarCodeSearchRequest request);
}


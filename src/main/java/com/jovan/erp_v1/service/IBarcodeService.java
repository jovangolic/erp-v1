package com.jovan.erp_v1.service;

import java.time.LocalDateTime;
import java.util.List;


import com.jovan.erp_v1.request.BarCodeRequest;
import com.jovan.erp_v1.response.BarCodeResponse;

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
}

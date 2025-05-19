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
	List<BarCodeResponse> getByGoods(Long goodsId);
	List<BarCodeResponse> getByScannedBy(String scannedBy); 
	List<BarCodeResponse> getByScannedAtBetween(LocalDateTime from, LocalDateTime to);
}

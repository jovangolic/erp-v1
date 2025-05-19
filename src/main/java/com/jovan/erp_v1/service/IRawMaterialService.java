package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.RawMaterialRequest;
import com.jovan.erp_v1.response.RawMaterialResponse;

public interface IRawMaterialService {

	List<RawMaterialResponse> findAll();
    RawMaterialResponse findById(Long id);
    RawMaterialResponse save(RawMaterialRequest request);
    RawMaterialResponse update(Long id, RawMaterialRequest request);
    void delete(Long id);

    // Ako želiš dodatne metode za pretragu
    List<RawMaterialResponse> findByName(String name);
	
}

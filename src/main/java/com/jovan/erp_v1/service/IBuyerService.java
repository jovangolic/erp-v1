package com.jovan.erp_v1.service;

import java.util.List;

import com.jovan.erp_v1.request.BuyerRequest;
import com.jovan.erp_v1.response.BuyerResponse;

public interface IBuyerService {

    BuyerResponse createBuyer(BuyerRequest request);

    BuyerResponse updateBuyer(String pib, BuyerRequest request);

    List<BuyerResponse> getAllBuyers();

    BuyerResponse getBuyerById(Long id);

    void deleteBuyer(Long id);

    List<BuyerResponse> searchBuyers(String keyword);

    BuyerResponse getBuyerByPid(String pib);
}

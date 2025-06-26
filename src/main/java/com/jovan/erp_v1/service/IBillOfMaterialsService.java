package com.jovan.erp_v1.service;

import java.math.BigDecimal;
import java.util.List;
import com.jovan.erp_v1.request.BillOfMaterialsRequest;
import com.jovan.erp_v1.response.BillOfMaterialsResponse;

public interface IBillOfMaterialsService {

    BillOfMaterialsResponse create(BillOfMaterialsRequest request);

    BillOfMaterialsResponse update(Long id, BillOfMaterialsRequest request);

    void delete(Long id);

    BillOfMaterialsResponse findOne(Long id);

    List<BillOfMaterialsResponse> findAll();

    List<BillOfMaterialsResponse> findByParentProductId(Long parentProductId);

    List<BillOfMaterialsResponse> findByComponentId(Long componentId);

    List<BillOfMaterialsResponse> findByQuantityGreaterThan(BigDecimal quantity);

    List<BillOfMaterialsResponse> findByQuantity(BigDecimal quantity);

    List<BillOfMaterialsResponse> findByQuantityLessThan(BigDecimal quantity);
}

package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;

/**
 * Converting between DTO and Entities
 */
@Mapper(componentModel = "Spring")
public interface VendorMapper {

    Vendor toEntity(VendorRequest request);

    VendorResponse toResponse(Vendor vendor);

    List<VendorResponse> toResponseList(List<Vendor> vendors);

}

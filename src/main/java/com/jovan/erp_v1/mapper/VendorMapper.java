package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.jovan.erp_v1.model.Vendor;
import com.jovan.erp_v1.request.VendorRequest;
import com.jovan.erp_v1.response.VendorResponse;

/**
 * Converting between DTO and Entities
 */

@Mapper(componentModel = "spring")
public interface VendorMapper {

    @Mapping(target = "materialTransactions", ignore = true)
    Vendor toEntity(VendorRequest request);

    VendorResponse toResponse(Vendor vendor);

    List<VendorResponse> toResponseList(List<Vendor> vendors);

}

/*
 * No implementation was created for VendorMapper due to having a problem in the
 * erroneous element null.
 * Hint: this often means that some other annotation processor was supposed to
 * process the erroneous element.
 * You can also enable MapStruct verbose mode by setting
 * -Amapstruct.verbose=true as a compilation argument.Java(0)
 */

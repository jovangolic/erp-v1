package com.jovan.erp_v1.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.jovan.erp_v1.model.Role;
import com.jovan.erp_v1.request.RoleRequest;
import com.jovan.erp_v1.response.RoleResponse;

@Mapper(componentModel = "Spring")
public interface RoleMapper {

	Role toEntity(RoleRequest request);

    RoleResponse toResponse(Role role);

    List<RoleResponse> toResponseList(List<Role> roles);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRole(RoleRequest request, @MappingTarget Role role);
}

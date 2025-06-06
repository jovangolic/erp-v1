package com.jovan.erp_v1.response;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import com.jovan.erp_v1.enumeration.RoleTypes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

    private Long id;
    private String name;
    private RoleTypes type;
    @Builder.Default
    private Set<PermissionResponse> permissions = new HashSet<>();
    @Builder.Default
    private List<UserResponse> users = new ArrayList<>();
}

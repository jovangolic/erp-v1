package com.jovan.erp_v1.response;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.jovan.erp_v1.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
// @NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseForEmployees {

    private Long id;
    // private String firstName;
    // private String lastName;
    private String phoneNumber;
    private String address;
    // private Collection<Role> roles = new HashSet<>();
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    public UserResponseForEmployees(User user) {
        this.id = user.getId();
        // this.firstName = user.getFirstName();
        // this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.roles = user.getRoles().stream()
                .map(role -> role.getName()) // pretvara enum u String
                .collect(Collectors.toSet());
    }
}

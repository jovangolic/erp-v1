package com.jovan.erp_v1.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

	private Long userId;
    private String email;
    private String accessToken;
    private String refreshToken;
    private List<String> roles;
}

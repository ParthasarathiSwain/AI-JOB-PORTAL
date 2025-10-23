package com.otz.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
}

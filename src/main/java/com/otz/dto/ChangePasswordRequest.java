package com.otz.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {
    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;
}

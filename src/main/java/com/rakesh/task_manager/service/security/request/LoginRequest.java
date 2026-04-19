package com.rakesh.task_manager.service.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    @Size(min = 4, max = 15)
    private String email;
    @NotBlank
    @Size(min = 4, max = 20)
    private String password;
}

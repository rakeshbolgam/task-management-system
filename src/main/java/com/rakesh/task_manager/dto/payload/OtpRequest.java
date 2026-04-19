package com.rakesh.task_manager.dto.payload;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String otp;
}

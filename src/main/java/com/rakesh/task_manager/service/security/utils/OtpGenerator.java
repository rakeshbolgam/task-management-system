package com.rakesh.task_manager.service.security.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class OtpGenerator {
    public String otpGenerator(){
        return String.valueOf(
                ThreadLocalRandom.current().nextInt(100000,999999)
        );
    }
}

package com.rakesh.task_manager.repo;

import com.rakesh.task_manager.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Integer> {
    Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
}

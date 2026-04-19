package com.rakesh.task_manager.config;

import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.repo.UsersRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        if(!usersRepo.existsByEmail("taskmanager.rakhii@gmail.com")){
            Users user=new Users();
            user.setUsername("admin");
            user.setEmail("taskmanager.rakhii@gmail.com");
            user.setPassword(passwordEncoder.encode("admin@123"));
            user.setRole("ROLE_ADMIN");
            user.setStatus("APPROVED");
            usersRepo.save(user);
        }
    }
}

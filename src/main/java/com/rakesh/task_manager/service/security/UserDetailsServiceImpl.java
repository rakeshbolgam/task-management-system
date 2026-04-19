package com.rakesh.task_manager.service.security;

import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private UserDetailsImpl userDetails;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with the specified email...")
                );
        UserDetailsImpl build = userDetails.build(users);
        return build;
    }
}

package com.rakesh.task_manager.service.security.utils;

import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.exceptions.APIException;
import com.rakesh.task_manager.repo.UsersRepo;
import com.rakesh.task_manager.service.security.UserDetailsImpl;
import com.rakesh.task_manager.service.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {

    @Autowired
    private UsersRepo usersRepo;

    public String loggedInEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            throw new APIException("Unauthorized");
        }
//        Users user = usersRepo.findByEmail(authentication.getName())
//                .orElseThrow(
//                        () -> new UsernameNotFoundException("User not found")
//                );
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return principal.getEmail();
    }

    public Users loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            throw new APIException("Unauthorized");
        }
//        Users user = usersRepo.findByEmail(authentication.getName())
//                .orElseThrow(
//                        () -> new APIException("User not found")
//                );
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return usersRepo.findById(principal.getId()).orElseThrow(
                ()->new APIException("User Not Found")
        );
    }

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new APIException("unauthorized");
        }
//        Users user = usersRepo.findByEmail(authentication.getName())
//                .orElseThrow(
//                        () -> new APIException("user not found"+authentication.getName())
//                );
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return principal.getId();
    }

}

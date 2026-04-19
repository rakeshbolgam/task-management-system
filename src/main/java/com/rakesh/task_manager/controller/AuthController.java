package com.rakesh.task_manager.controller;

import com.rakesh.task_manager.dto.payload.MessageResponse;
import com.rakesh.task_manager.dto.payload.OtpRequest;
import com.rakesh.task_manager.dto.payload.UserRequest;
import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.repo.UsersRepo;
import com.rakesh.task_manager.service.otp.OtpService;
import com.rakesh.task_manager.service.security.UserDetailsImpl;
import com.rakesh.task_manager.service.security.jwt.JwtUtils;
import com.rakesh.task_manager.service.security.request.LoginRequest;
import com.rakesh.task_manager.service.security.response.UserInfoResponse;
import com.rakesh.task_manager.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OtpService otpService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UsersRepo usersRepo;
    //    @PostMapping("/create")
//    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO){
//        userService.createUser(userDTO);
////        return new ResponseEntity<>("User created successfully!!!", HttpStatus.CREATED);
//        return ResponseEntity.status(HttpStatus.CREATED).body("User Created Successfully!!!");
//    }
    @PostMapping("/register")
    public ResponseEntity<?> userRegistration(@RequestBody @Valid UserRequest request){
        ResponseEntity<MessageResponse> user = userService.createUser(request);
//        MessageResponse body = user.getBody();
        if(user.getBody().isSuccess()){
            return new ResponseEntity<>(user.getBody(),HttpStatus.CREATED);
        }
        MessageResponse response=new MessageResponse();
//        if(userDto!=null){
//            response.setMessage("OTP sent to mail");
//            response.setSuccess(true);
//            otpService.sendOtp(request.getEmail());
//            return new ResponseEntity<>(response,HttpStatus.OK);
//        }
        response.setMessage("Something went wrong...");
        response.setSuccess(false);
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest request){
        otpService.verifyOtp(request);
        return new ResponseEntity<>(new MessageResponse("Email Verified Successfully",true),HttpStatus.OK);
    }
    @PostMapping("/resend-otp/{email}")
    public ResponseEntity<?> resendOtp(@PathVariable String email){
        otpService.sendOtp(email);
        return new ResponseEntity<>(new MessageResponse("OTP Sent Successfully....",true),HttpStatus.OK);
    }

    @PostMapping("/sign-in/user")
    public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest){
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()
                    )
            );
//            Users users = usersRepo.findByEmail(loginRequest.getEmail())
//                    .orElseThrow(() -> new RuntimeException("User not found with given mail..."));

        } catch (AuthenticationException e) {
            Map<String,Object> body = new HashMap<>();
            body.put("message","Bad Credentials");
            body.put("status",false);

            return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Object principal = authentication.getPrincipal();
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());


        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),
                jwtToken,
                roles);
        return ResponseEntity.ok().body(userInfoResponse);
    }

}

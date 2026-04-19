package com.rakesh.task_manager.controller;

import com.rakesh.task_manager.constants.AppConstant;
import com.rakesh.task_manager.dto.UserDTO;
import com.rakesh.task_manager.dto.UserResponseDTO;
import com.rakesh.task_manager.dto.payload.MessageResponse;
import com.rakesh.task_manager.dto.payload.UserRequest;
import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.service.security.utils.AuthUtil;
import com.rakesh.task_manager.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthUtil authUtil;
    //update user
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserRequest request, @RequestParam Long userId){
        UserDTO userDTO = userService.updateUser(request, userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    //get list of users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-All")
    public ResponseEntity<UserResponseDTO> getALlUsers(@RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                         @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
                                         @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_BY) String sortBy,
                                         @RequestParam(name = "sortDir", defaultValue = AppConstant.SORT_DIR) String sortDir){
        Long userId = authUtil.loggedInUserId();

        UserResponseDTO userResponseDTO = userService.getAll(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(userResponseDTO);
    }
    //get user by email
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get")
    public ResponseEntity<?> getUserDetails(@RequestParam(required = false) Long id){
        Users userById = userService.getUserById(id);
        return ResponseEntity.ok(userById);
    }

    //delete user
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        userService.deleteById(id);
        MessageResponse response=new MessageResponse();
        response.setMessage("User is deleted from DB");
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}

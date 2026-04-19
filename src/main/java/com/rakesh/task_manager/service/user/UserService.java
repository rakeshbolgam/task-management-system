package com.rakesh.task_manager.service.user;

import com.rakesh.task_manager.dto.UserDTO;
import com.rakesh.task_manager.dto.UserResponseDTO;
import com.rakesh.task_manager.dto.payload.MessageResponse;
import com.rakesh.task_manager.dto.payload.UserRequest;
import com.rakesh.task_manager.entity.Users;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<MessageResponse> createUser(UserRequest request);

    Users getUserById(Long id);

    void deleteById(Long id);

    UserDTO updateUser(UserRequest request, Long userId);

    UserResponseDTO getAll(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}

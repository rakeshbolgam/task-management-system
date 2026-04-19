package com.rakesh.task_manager.serviceimpl.userImpl;

import com.rakesh.task_manager.dto.UserDTO;
import com.rakesh.task_manager.dto.UserResponseDTO;
import com.rakesh.task_manager.dto.payload.MessageResponse;
import com.rakesh.task_manager.dto.payload.UserRequest;
import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.exceptions.APIException;
import com.rakesh.task_manager.repo.UsersRepo;
import com.rakesh.task_manager.service.otp.OtpService;
import com.rakesh.task_manager.service.security.utils.AuthUtil;
import com.rakesh.task_manager.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private final PasswordEncoder passwordEncoder;
    private final UsersRepo usersRepo;
    private final OtpService otpService;
    private final AuthUtil authUtil;

    @Override
    public ResponseEntity<MessageResponse> createUser(UserRequest request) {

        if (usersRepo.existsByEmail(request.getEmail())) {
            throw new APIException("user already exists with this email");
        }

        Users user = dtoToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus("OTP_PENDING");
        usersRepo.save(user);

        otpService.sendOtp(request.getEmail());

        MessageResponse response = new MessageResponse();
        response.setMessage("OTP Sent successfully");
        response.setSuccess(true);

        return ResponseEntity.ok(response);
//        Optional<Users> byMail = usersRepo.findByEmail(request.getEmail());
//        if(byMail.isPresent())
//            throw new RuntimeException("The User with this mail already exists...");
//        Users users = dtoToEntity(request);
//        users.setPassword(passwordEncoder.encode(users.getPassword()));
//        users.setStatus("OTP_PENDING");
//        MessageResponse response=new MessageResponse();
//        response.setMessage("OTP Sent to Mail...");
//        response.setSuccess(true);
//        otpService.sendOtp(request.getEmail());
//        Users save = usersRepo.save(users);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    @Override
//    public List<Users> getAll() {
//        List<Users> all = usersRepo.findAll();
//        return all;
//    }

    @Override
    public Users getUserById(Long id) {
        Long userId;
        if(id==null){
            userId = authUtil.loggedInUserId();
        }else{
            boolean isAdmin = authUtil.loggedInUser().getRole().equals("ROLE_ADMIN");
            if(!isAdmin)
                throw new APIException("Access Denied");
            userId = id;
        }
        Users user = usersRepo.findById(userId).orElseThrow(
                () -> new APIException("Id not found..."));
        return user;
    }

    @Override
    public void deleteById(Long id) {
        usersRepo.deleteById(id);
    }

    @Override
    public UserDTO updateUser(UserRequest request, Long userId) {
        Long loggedInUserId ;
        if(userId == null){
            loggedInUserId = authUtil.loggedInUserId();
        }else{
            boolean isAdmin = authUtil.loggedInUser().getRole().equals("ROLE_ADMIN");
            if(!isAdmin)
                throw new APIException("Access Denied...");
            loggedInUserId = userId;
        }
        Users existingUser = usersRepo.findById(loggedInUserId).orElseThrow(() -> new APIException("UserId Not Found..."));
        existingUser.setUsername(request.getUsername());
        existingUser.setRole(request.getRole());
        Users save = usersRepo.save(existingUser);
        return entityToUserDTO(save);
    }

    @Override
    public UserResponseDTO getAll(Long userId, Integer pageNumber, Integer pageSize, String sortByEmail, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortByEmail).ascending() : Sort.by(sortByEmail).descending();
        //Creating Pageable for pagination
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
//        Page<Users> pageOfUsers = usersRepo.findById(userId, pageRequest);
        Page<Users> pageOfUsers = usersRepo.findAll(pageRequest);
        List<Users> listOfUsers = pageOfUsers.getContent();
        if(listOfUsers.isEmpty())
            return new UserResponseDTO();
        List<UserDTO> list = listOfUsers.stream()
                .map(
                        user -> entityToUserDTO(user)
                )
                .toList();
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setContent(list);
        responseDTO.setPageNumber(pageOfUsers.getNumber());
        responseDTO.setPageSize(pageOfUsers.getSize());
        responseDTO.setTotalPages(pageOfUsers.getTotalPages());
        responseDTO.setLastPage(pageOfUsers.isLast());
        responseDTO.setTotalElements(pageOfUsers.getTotalElements());

        return responseDTO;
    }

    public Users dtoToEntity(UserRequest request) {
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole("ROLE_USER");
        return user;
    }

    public UserDTO entityToUserDTO(Users user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsreId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserName(user.getUsername());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}
